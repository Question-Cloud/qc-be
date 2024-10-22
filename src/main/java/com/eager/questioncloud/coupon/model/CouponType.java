package com.eager.questioncloud.coupon.model;

import lombok.Getter;

@Getter
public enum CouponType {
    Percent("Percent"), Fixed("Fixed");

    private String value;

    CouponType(String value) {
        this.value = value;
    }
}
