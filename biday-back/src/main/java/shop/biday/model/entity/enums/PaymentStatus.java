package shop.biday.model.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    // ENUM: 'pending', 'completed', 'failed'
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private final String paymentStatus;

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
