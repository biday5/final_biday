package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.biday.exception.PaymentException;
import shop.biday.model.domain.*;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.dto.PaymentRequest;
import shop.biday.model.dto.PaymentResponse;
import shop.biday.model.entity.AwardEntity;
import shop.biday.model.entity.PaymentEntity;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.entity.enums.PaymentCardType;
import shop.biday.model.entity.enums.PaymentMethod;
import shop.biday.model.entity.enums.PaymentStatus;
import shop.biday.model.repository.PaymentRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.AwardService;
import shop.biday.service.PaymentService;
import shop.biday.service.UserService;
import shop.biday.utils.RedisTemplateUtils;
import shop.biday.utils.TossPaymentTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final String APPROVE_URI = "confirm";

    private final UserService userService;
    private final AwardService awardService;
    private final PaymentRepository paymentRepository;
    private final TossPaymentTemplate tossPaymentTemplate;
    private final RedisTemplateUtils<PaymentTempModel> redisTemplateUtils;
    private final JWTUtil jWTUtil;

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
        UserEntity user = userService.findById(paymentRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자입니다."));

        AwardEntity award = awardService.findById(paymentRequest.awardId());

        PaymentTempModel paymentTempModel = getPaymentTempModel(paymentRequest);
        if (!isCheckPaymentData(paymentRequest, paymentTempModel)) {
            return false;
        }

        ResponseEntity<PaymentModel> response = tossPaymentTemplate.exchangePostMethod(APPROVE_URI, paymentRequest);
        PaymentModel paymentModel = tossPaymentTemplate.getPayment(response);

        ZonedDateTime requestedAt = ZonedDateTime.parse(paymentModel.getRequestedAt(), DATE_TIME_FORMATTER);
        ZonedDateTime approvedAt = ZonedDateTime.parse(paymentModel.getApprovedAt(), DATE_TIME_FORMATTER);

        PaymentEntity payment = PaymentEntity.builder()
                .user(user)
                .award(award)
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
        deletePaymentTempModel(paymentRequest.orderId());
        return true;
    }

    @Override
    public PaymentResponse findPaymentByPaymentKey(Long id) {
        PaymentEntity payment = findById(id);
        log.info("payment: {}", payment);

        ResponseEntity<PaymentModel> response = tossPaymentTemplate.exchangeGetMethod(payment.getPaymentKey());
        PaymentModel paymentModel = tossPaymentTemplate.getPayment(response);

        PaymentCardModel card = paymentModel.getCard();
        card.setIssuerName(PaymentCardType.getByCode(card.getIssuerCode()).getName());

        ZonedDateTime approvedAt = ZonedDateTime.parse(paymentModel.getApprovedAt(), DATE_TIME_FORMATTER);

        //TODO : Role 변화 시 주석 해제 필요
        UserModel userModel = UserModel.builder()
                .id(payment.getUser().getId())
                .email(payment.getUser().getEmail())
                .name(payment.getUser().getName())
                .phoneNum(payment.getUser().getPhone())
//                .role(payment.getUser().getRole())
                .build();
//        AuctionModel auctionModel = AuctionModel.builder()
//                .id(payment.getAward().getAuction().getId())
//                .startingBid(payment.getAward().getAuction().getStartingBid())
//                .currentBid(payment.getAward().getAuction().getCurrentBid())
//                .build();
        AuctionDto auctionDto = AuctionDto.builder()
                .id(payment.getAward().getAuction().getId())
                .startingBid(payment.getAward().getAuction().getStartingBid())
                .currentBid(payment.getAward().getAuction().getCurrentBid())
                .build();
        AwardModel awardModel = AwardModel.builder()
                .id(payment.getAward().getId())
//                .auction(auctionModel)
                .auction(auctionDto)
                .bidedAt(payment.getAward().getBidedAt())
                .currentBid(payment.getAward().getCurrentBid())
                .count(payment.getAward().getCount())
                .build();

        return new PaymentResponse(
                payment.getId(),
                userModel,
                awardModel,
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

    // TODO user mongo로 올리면 변경 해야 함
    @Override
    public List<PaymentRequest> findByUser(String token) {
        String user = jWTUtil.getEmail(token);
        log.info("Find Payment By User: {}", user);
        if (!userService.existsById(Long.valueOf(user))) {
            log.error("User not found");
            return null;
        } else {
            return paymentRepository.findByUser(user);
        }
    }

    private boolean isCheckPaymentData(PaymentRequest paymentRequest, PaymentTempModel paymentTempModel) {
        if (paymentTempModel == null) {
            return false;
        }
        return Objects.equals(paymentTempModel.orderId(), paymentRequest.orderId()) &&
                Objects.equals(paymentTempModel.userId(), paymentRequest.userId()) &&
                Objects.equals(paymentTempModel.awardId(), paymentRequest.awardId()) &&
                Objects.equals(paymentTempModel.amount(), paymentRequest.amount());
    }

    private void deletePaymentTempModel(String key) {
        redisTemplateUtils.delete(key);
    }

    private PaymentTempModel getPaymentTempModel(PaymentRequest paymentRequest) {
        return redisTemplateUtils.get(paymentRequest.orderId(), PaymentTempModel.class);
    }
}
