package shop.biday.model.dto;

import shop.biday.model.domain.AwardModel;
import shop.biday.model.domain.PaymentCardModel;
import shop.biday.model.domain.PaymentEasyPay;
import shop.biday.model.domain.UserModel;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        UserModel user,
        AwardModel award,
        Long amount,
        String method,
        String orderId,
        String status,
        PaymentCardModel card,
        PaymentEasyPay easyPay,
        LocalDateTime approvedAt
) {
}
