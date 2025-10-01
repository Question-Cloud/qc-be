package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import java.time.LocalDateTime

class UserCoupon(
    val id: Long = 0,
    val userId: Long,
    val couponId: Long,
    var isUsed: Boolean,
    var usedOrderId: String? = null,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    companion object {
        fun create(userId: Long, couponInformation: CouponInformation): UserCoupon {
            if (couponInformation.endAt.isBefore(LocalDateTime.now())) {
                throw CoreException(Error.EXPIRED_COUPON)
            }
            if (couponInformation.remainingCount == 0) {
                throw CoreException(Error.LIMITED_COUPON)
            }
            return UserCoupon(
                userId = userId,
                couponId = couponInformation.id,
                isUsed = false,
                createdAt = LocalDateTime.now(),
                endAt = couponInformation.endAt
            )
        }
    }
}
