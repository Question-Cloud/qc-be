package com.eager.questioncloud.core.domain.pg;

import lombok.Getter;

@Getter
public class PGPayment {
    private final String paymentId;
    private final int amount;
    private final String receiptUrl;

    public PGPayment(String paymentId, int amount, String receiptUrl) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.receiptUrl = receiptUrl;
    }
}
