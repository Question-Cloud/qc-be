package com.eager.questioncloud.core.domain.coupon.vo;

import lombok.Getter;

@Getter
public enum CouponType {
    Percent("Percent"), Fixed("Fixed");

    private final String value;

    CouponType(String value) {
        this.value = value;
    }
}
