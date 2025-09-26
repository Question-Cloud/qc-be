package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import java.time.LocalDateTime

class UserCoupon(
    var id: Long = 0,
    var userId: Long,
    var couponId: Long,
    var isUsed: Boolean,
    var usedOrderId: String? = null,
    var createdAt: LocalDateTime,
    var endAt: LocalDateTime,
) {
    fun validate() {
        if (endAt.isBefore(LocalDateTime.now())) {
            throw CoreException(Error.EXPIRED_COUPON)
        }
        
        if (this.isUsed) {
            throw CoreException(Error.FAIL_USE_COUPON)
        }
    }
    
    companion object {
        fun create(userId: Long, coupon: Coupon): UserCoupon {
            if (coupon.endAt.isBefore(LocalDateTime.now())) {
                throw CoreException(Error.EXPIRED_COUPON)
            }
            if (coupon.remainingCount == 0) {
                throw CoreException(Error.LIMITED_COUPON)
            }
            return UserCoupon(
                userId = userId,
                couponId = coupon.id,
                isUsed = false,
                createdAt = LocalDateTime.now(),
                endAt = coupon.endAt
            )
        }
    }
}
