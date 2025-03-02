package com.eager.questioncloud.core.domain.point.enums;

import lombok.Getter;

@Getter
public enum ChargePointPaymentStatus {
    ORDERED("ORDERED"), PAID("PAID"), Fail("Fail");

    private final String value;

    ChargePointPaymentStatus(String value) {
        this.value = value;
    }
}
