package shop.biday.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    CREDIT_CARD("CREDIT_CARD", "카드"),
    EASY_PAY("EASY_PAY", "간편결제"),
    BANK_TRANSFER("BANK_TRANSFER", "계좌이체");

    private final String method;
    private final String name;
}
