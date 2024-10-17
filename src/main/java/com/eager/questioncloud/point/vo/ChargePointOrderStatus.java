package com.eager.questioncloud.point.vo;

import lombok.Getter;

@Getter
public enum ChargePointOrderStatus {
    ORDERED("ORDERED"), PAID("PAID");

    private final String value;

    ChargePointOrderStatus(String value) {
        this.value = value;
    }
}
