package com.eager.questioncloud.core.domain.payment.model

import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.coupon.model.Coupon
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import kotlin.math.floor
import kotlin.math.max

class QuestionPaymentCoupon(
    var userCouponId: Long,
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
                coupon.title,
                coupon.couponType,
                coupon.value
            )
        }
    }
}
