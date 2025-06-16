package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.coupon.domain.Coupon
import com.eager.questioncloud.coupon.enums.CouponType
import kotlin.math.floor
import kotlin.math.max

class QuestionPaymentCoupon(
    var userCouponId: Long,
    var couponId: Long,
    var title: String,
    var couponType: CouponType,
    var value: Int,
) {
    fun calcDiscount(originalAmount: Int): Int {
        if (couponType == CouponType.Fixed) {
            return max((originalAmount - value).toDouble(), 0.0).toInt()
        }

        if (couponType == CouponType.Percent) {
            val discountRate = value / 100.0
            val discountAmount = floor(originalAmount * discountRate).toInt()
            return originalAmount - discountAmount
        }

        throw CoreException(Error.FAIL_USE_COUPON)
    }

    companion object {
        fun create(userCouponId: Long, coupon: Coupon): QuestionPaymentCoupon {
            return QuestionPaymentCoupon(
                userCouponId,
                coupon.id,
                coupon.title,
                coupon.couponType,
                coupon.value
            )
        }
    }
}
