package com.eager.questioncloud.coupon;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterCouponRequest {
        @NotBlank
        private String code;
    }
}
