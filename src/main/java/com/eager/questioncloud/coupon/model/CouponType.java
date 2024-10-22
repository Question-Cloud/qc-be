package com.eager.questioncloud.coupon.model;

import lombok.Getter;

@Getter
public enum CouponType {
    Percent("Percent"), Fixed("Fixed");

    private final String value;

    CouponType(String value) {
        this.value = value;
    }
}
