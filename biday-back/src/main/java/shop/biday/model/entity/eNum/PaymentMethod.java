package shop.biday.model.entity.eNum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    // ENUM: 'credit_card', 'paypal', 'bank_transfer'
    CREDIT_CARD("CREDIT_CARD"),
    PAYPAL("PAYPAL"),
    BANK_TRANSFER("BANK_TRANSFER");

    private final String paymentMethod;

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
