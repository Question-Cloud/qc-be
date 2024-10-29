package com.eager.questioncloud.core.domain.user.vo;

import lombok.Getter;

@Getter
public enum ChargePointOrderStatus {
    ORDERED("ORDERED"), PAID("PAID");

    private final String value;

    ChargePointOrderStatus(String value) {
        this.value = value;
    }
}
