package shop.biday.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.biday.exception.PaymentException;
import shop.biday.model.domain.PaymentCardModel;
import shop.biday.model.domain.PaymentModel;
import shop.biday.model.domain.PaymentTempModel;
import shop.biday.model.dto.PaymentRequest;
import shop.biday.model.dto.PaymentResponse;
import shop.biday.model.entity.PaymentEntity;
import shop.biday.model.entity.enums.PaymentCardType;
import shop.biday.model.entity.enums.PaymentMethod;
import shop.biday.model.entity.enums.PaymentStatus;
import shop.biday.model.repository.PaymentRepository;
import shop.biday.service.PaymentService;
import shop.biday.utils.RedisTemplateUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final String APPROVE_URI = "confirm";

    @Value("${payments.toss.secret.key}")
    private String widgetSecretKey;

    private final PaymentRepository paymentRepository;
    private final RedisTemplateUtils<PaymentTempModel> redisTemplateUtils;
    private final RestTemplate restTemplate;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              RedisTemplateUtils<PaymentTempModel> redisTemplateUtils,
                              RestTemplateBuilder restTemplateBuilder) {
        this.paymentRepository = paymentRepository;
        this.redisTemplateUtils = redisTemplateUtils;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public PaymentEntity findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentException(
                        HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청입니다.")
                );
    }

    @Override
    public boolean existsById(Long id) {
        return paymentRepository.existsById(id);
    }

    @Override
    public long count() {
        return paymentRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public void delete(PaymentEntity paymentEntity) {
        paymentRepository.delete(paymentEntity);
    }

    @Override
    public void savePaymentTemp(PaymentTempModel paymentTempModel) {
        redisTemplateUtils.save(paymentTempModel.orderId(), paymentTempModel);
    }

    @Override
    public Boolean save(PaymentRequest paymentRequest) {
        PaymentTempModel paymentTempModel = getPaymentTempModel(paymentRequest);
        if (!isCheckPaymentData(paymentRequest, paymentTempModel)) {
            return false;
        }

        URI approveUri = getUri(APPROVE_URI);
        ResponseEntity<PaymentModel> response = exchangePostMethod(approveUri, paymentRequest);
        PaymentModel paymentModel = getPayment(response);

        ZonedDateTime requestedAt = ZonedDateTime.parse(paymentModel.getRequestedAt(), DATE_TIME_FORMATTER);
        ZonedDateTime approvedAt = ZonedDateTime.parse(paymentModel.getApprovedAt(), DATE_TIME_FORMATTER);

        PaymentEntity payment = PaymentEntity.builder()
                .userId(paymentRequest.userId())
                .bidId(paymentRequest.bidId())
                .paymentKey(paymentModel.getPaymentKey())
                .type(paymentModel.getType())
                .orderId(paymentRequest.orderId())
                .currency(paymentModel.getCurrency())
                .paymentMethod(PaymentMethod.fromName(paymentModel.getMethod()))
                .totalAmount(paymentModel.getTotalAmount())
                .balanceAmount(paymentModel.getBalanceAmount())
                .paymentStatus(PaymentStatus.fromStatus(paymentModel.getStatus()))
                .requestedAt(requestedAt.toLocalDateTime())
                .approvedAt(approvedAt.toLocalDateTime())
                .suppliedAmount(paymentModel.getSuppliedAmount())
                .vat(paymentModel.getVat())
                .build();

        paymentRepository.save(payment);
        return true;
    }

    @Override
    public PaymentResponse findPaymentByPaymentKey(Long id) {
        PaymentEntity payment = findById(id);

        URI paymentKeyUri = getUri(payment.getPaymentKey());

        ResponseEntity<PaymentModel> response = exchangeGetMethod(paymentKeyUri);
        PaymentModel paymentModel = getPayment(response);

        PaymentCardModel card = paymentModel.getCard();
        card.setIssuerName(PaymentCardType.getByCode(card.getIssuerCode()).getName());

        ZonedDateTime approvedAt = ZonedDateTime.parse(paymentModel.getApprovedAt(), DATE_TIME_FORMATTER);
        return new PaymentResponse(
                payment.getId(),
                payment.getUserId(),
                payment.getBidId(),
                paymentModel.getTotalAmount(),
                paymentModel.getMethod(),
                paymentModel.getOrderId(),
                paymentModel.getStatus(),
                card,
                paymentModel.getEasyPay(),
                approvedAt.toLocalDateTime()
        );
    }

    @Override
    public PaymentEntity updateCancelStatus(Long id, PaymentStatus paymentStatus) {
        PaymentEntity payment = findById(id);
        payment.setPaymentStatus(paymentStatus);
        return paymentRepository.save(payment);
    }

    private PaymentModel getPayment(ResponseEntity<PaymentModel> response) {
        PaymentModel paymentModel = response.getBody();
        if (paymentModel == null) {
            throw new PaymentException(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청입니다.");
        }
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            throw new PaymentException(
                    HttpStatus.valueOf(response.getStatusCode().value()),
                    paymentModel.getCode(),
                    paymentModel.getMessage());
        }

        return paymentModel;
    }

    private ResponseEntity<PaymentModel> exchangeGetMethod(URI uri) {
        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                PaymentModel.class);
    }

    private <T> ResponseEntity<PaymentModel> exchangePostMethod(URI uri, T request) {
        return restTemplate.exchange(
                uri,
                HttpMethod.POST,
                new HttpEntity<>(request, getHttpHeaders()),
                PaymentModel.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(getAuthorizationHeader());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private URI getUri(String path) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.tosspayments.com")
                .path("/v1/payments/" + path)
                .build()
                .encode()
                .toUri();
    }

    private String getAuthorizationHeader() {
        String authorization = widgetSecretKey + ":";
        return new String(Base64.getEncoder().encode(authorization.getBytes(StandardCharsets.UTF_8)));
    }

    private boolean isCheckPaymentData(PaymentRequest paymentRequest, PaymentTempModel paymentTempModel) {
        if (paymentTempModel == null) {
            return false;
        }
        return Objects.equals(paymentTempModel.orderId(), paymentRequest.orderId()) &&
                Objects.equals(paymentTempModel.userId(), paymentRequest.userId()) &&
                Objects.equals(paymentTempModel.bidId(), paymentRequest.bidId()) &&
                Objects.equals(paymentTempModel.amount(), paymentRequest.amount());
    }

    private PaymentTempModel getPaymentTempModel(PaymentRequest paymentRequest) {
        return redisTemplateUtils.get(paymentRequest.orderId(), PaymentTempModel.class);
    }
}
