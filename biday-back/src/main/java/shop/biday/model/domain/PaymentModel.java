package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel {

    private String paymentKey;
    private String type;
    private String orderId;
    private String currency;
    private String method;
    private Long totalAmount;
    private Long balanceAmount;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private Long suppliedAmount;
    private Long vat;
    private List<PaymentCancelModel> cancels;
    private PaymentCardModel card;
    private PaymentEasyPay easyPay;
    private String country;
    private PaymentFailure failure;

    private String code;
    private String message;
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