package com.eager.questioncloud.portone;

import lombok.Getter;

@Getter
public enum PortonePaymentStatus {
    CANCELLED("CANCELLED"), FAILED("FAILED"), PAID("PAID"), PARTIAL_CANCELLED("PARTIAL_CANCELLED");
    private final String value;

    PortonePaymentStatus(String value) {
        this.value = value;
    }
}
