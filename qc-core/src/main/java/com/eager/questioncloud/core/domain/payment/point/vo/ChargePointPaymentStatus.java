package com.eager.questioncloud.core.domain.payment.point.vo;

import lombok.Getter;

@Getter
public enum ChargePointPaymentStatus {
    ORDERED("ORDERED"), PAID("PAID"), Fail("Fail");

    private final String value;

    ChargePointPaymentStatus(String value) {
        this.value = value;
    }
}
