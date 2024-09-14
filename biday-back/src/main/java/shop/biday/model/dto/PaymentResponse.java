package shop.biday.model.dto;

import shop.biday.model.domain.PaymentCardModel;
import shop.biday.model.domain.PaymentEasyPay;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long userId,
        Long bidId,
        Long amount,
        String method,
        String orderId,
        String status,
        PaymentCardModel card,
        PaymentEasyPay easyPay,
        LocalDateTime approvedAt
) {
}
