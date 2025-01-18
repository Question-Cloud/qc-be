package com.eager.questioncloud.application.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FailChargePointPaymentMessage {
    private int failCount;
    private String paymentId;

    public static FailChargePointPaymentMessage create(String paymentId) {
        return new FailChargePointPaymentMessage(0, paymentId);
    }

    public void increaseFailCount() {
        this.failCount++;
    }
}
