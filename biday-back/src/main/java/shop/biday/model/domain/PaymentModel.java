package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.enums.PaymentMethod;
import shop.biday.model.entity.enums.PaymentStatus;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel {
    private Long id;
    private Long userId;
    private Long bidId;
    private Long amount;
    private Long finalBid;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
/*
{
  "id", // 결제 id
  "orderId", // 주문번호
  "status", // 결제 처리 상태
  "approvedAt", // 결제 승인일
  "card": {
    "issuerCode", // 카드 발급사
    "installmentPlanMonths", // 할부 개월 수
    "cardType", // 카드 종류
  },
  "easyPay": {
    "provider", // 간편결제사
  },
  "totalAmount", // 총 결제 금액
  "method" // 결제 수단
}
*/