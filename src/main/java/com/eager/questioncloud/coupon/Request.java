package com.eager.questioncloud.coupon;

import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterCouponRequest {
        private String code;
    }
}
