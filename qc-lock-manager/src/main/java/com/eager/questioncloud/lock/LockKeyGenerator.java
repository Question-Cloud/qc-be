package com.eager.questioncloud.lock;

public class LockKeyGenerator {
    public static String generateRegisterCouponKey(Long userId, String couponCode) {
        return "REGISTER-COUPON-" + userId + "-" + couponCode;
    }

    public static String generateCreatorStatistics(Long creatorId) {
        return "CREATOR-STATISTICS-" + creatorId;
    }

    public static String generateReviewStatistics(Long questionId) {
        return "REVIEW-STATISTICS-" + questionId;
    }
}
