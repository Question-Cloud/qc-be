package com.eager.questioncloud.lock

object LockKeyGenerator {
    @JvmStatic
    fun generateRegisterCouponKey(userId: Long, couponCode: String): String {
        return "REGISTER-COUPON-$userId-$couponCode"
    }

    @JvmStatic
    fun generateCreatorStatistics(creatorId: Long): String {
        return "CREATOR-STATISTICS-$creatorId"
    }

    @JvmStatic
    fun generateReviewStatistics(questionId: Long): String {
        return "REVIEW-STATISTICS-$questionId"
    }
}
