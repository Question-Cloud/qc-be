package com.eager.questioncloud.lock;

public class LockKeyGenerator {
    public static String generateRegisterCouponKey(Long userId, String couponCode) {
        return "REGISTER-COUPON-" + userId + "-" + couponCode;
    }

    public static String generateChargePointPaymentKey(String paymentId) {
        return "CHARGE-POINT-PAYMENT-" + paymentId;
    }

    public static String generateQuestionPaymentKey(Long userId) {
        return "QUESTION-PAYMENT-" + userId;
    }

    public static String generateRegisterReview(Long userId, Long questionId) {
        return "REGISTER-REVIEW-" + userId + "-" + questionId;
    }

    public static String generateCreatorStatistics(Long creatorId) {
        return "CREATOR-STATISTICS-" + creatorId;
    }

    public static String generateReviewStatistics(Long questionId) {
        return "REVIEW-STATISTICS-" + questionId;
    }
}
