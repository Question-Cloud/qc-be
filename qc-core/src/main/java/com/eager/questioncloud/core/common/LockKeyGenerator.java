package com.eager.questioncloud.core.common;

public class LockKeyGenerator {
    public static String generateRegisterCouponKey(Long userId, String couponCode) {
        return "REGISTER-COUPON-" + userId + "-" + couponCode;
    }
}
