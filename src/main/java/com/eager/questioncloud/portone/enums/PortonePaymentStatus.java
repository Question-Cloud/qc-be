package com.eager.questioncloud.portone.enums;

import lombok.Getter;

@Getter
public enum PortonePaymentStatus {
    READY("READY"), CANCELLED("CANCELLED"), FAILED("FAILED"), PAID("PAID"), PARTIAL_CANCELLED("PARTIAL_CANCELLED");
    private final String value;

    PortonePaymentStatus(String value) {
        this.value = value;
    }
}
