package com.eager.questioncloud.lock

object LockKeyGenerator {
    fun generateRegisterCouponKey(userId: Long, couponCode: String): String {
        return "REGISTER-COUPON-$userId-$couponCode"
    }
}
