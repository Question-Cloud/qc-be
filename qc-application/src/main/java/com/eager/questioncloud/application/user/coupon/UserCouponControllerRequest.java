package com.eager.questioncloud.application.user.coupon;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class UserCouponControllerRequest {
    @Getter
    public static class RegisterCouponRequest {
        @NotBlank
        private String code;
    }
}
