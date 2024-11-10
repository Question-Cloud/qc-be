package com.eager.questioncloud.api.user.coupon;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterCouponRequest {
        @NotBlank
        private String code;
    }
}
