package shop.biday.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.biday.exception.PaymentException;
import shop.biday.model.domain.PaymentCancelModel;
import shop.biday.model.domain.PaymentModel;
import shop.biday.model.dto.RefundRequest;
import shop.biday.model.entity.PaymentEntity;
import shop.biday.model.entity.RefundEntity;
import shop.biday.model.entity.enums.PaymentStatus;
import shop.biday.model.repository.RefundRepository;
import shop.biday.service.PaymentService;
import shop.biday.service.RefundService;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class RefundServiceImpl implements RefundService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Value("${payments.toss.secret.key}")
    private String widgetSecretKey;

    private final RestTemplate restTemplate;
    private final PaymentService paymentService;
    private final RefundRepository refundRepository;

    public RefundServiceImpl(RestTemplateBuilder restTemplateBuilder,
                             PaymentService paymentService, RefundRepository refundRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.paymentService = paymentService;
        this.refundRepository = refundRepository;
    }

    @Override
    public boolean cancel(Long id, RefundRequest cancelRequest) {
        PaymentEntity payment = paymentService.findById(id);
        URI cancelUri = getUri(String.format("%s/cancel", payment.getPaymentKey()));

        ResponseEntity<PaymentModel> response = exchangePostMethod(cancelUri, cancelRequest);

        PaymentModel paymentModel = getPayment(response);
        PaymentCancelModel cancel = getCancel(paymentModel.getCancels());

        ZonedDateTime canceledAt = ZonedDateTime.parse(cancel.getCanceledAt(), DATE_TIME_FORMATTER);
        RefundEntity refundEntity = RefundEntity.builder()
                .payment(payment)
                .transactionKey(cancel.getTransactionKey())
                .reason(cancel.getCancelReason())
                .canceledAt(canceledAt.toLocalDateTime())
                .amount(cancel.getCancelAmount())
                .status(cancel.getCancelStatus())
                .build();

        refundRepository.save(refundEntity);
        paymentService.updateCancelStatus(id, PaymentStatus.fromStatus(paymentModel.getStatus()));
        return true;
    }

    private PaymentCancelModel getCancel(List<PaymentCancelModel> cancels) {
        return PaymentCancelModel.builder()
                .transactionKey(cancels.get(0).getTransactionKey())
                .cancelReason(cancels.get(0).getCancelReason())
                .cancelAmount(cancels.get(0).getCancelAmount())
                .canceledAt(cancels.get(0).getCanceledAt())
                .cancelStatus(cancels.get(0).getCancelStatus())
                .build();
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
}
