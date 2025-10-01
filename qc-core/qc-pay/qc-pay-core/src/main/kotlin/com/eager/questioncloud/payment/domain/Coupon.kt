package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import kotlin.math.floor
import kotlin.math.min

abstract class Coupon(
    val couponInformation: CouponInformation,
    val userCoupon: UserCoupon,
) : Discountable {
    var appliedDiscountAmount: Int = 0
    
    abstract fun isApplicable(questionPayment: QuestionPayment): Boolean
    
    abstract fun doApply(questionPayment: QuestionPayment)
    
    fun apply(questionPayment: QuestionPayment) {
        if (!isApplicable(questionPayment)) {
            throw CoreException(Error.WRONG_COUPON)
        }
        return doApply(questionPayment)
    }
    
    override fun getDiscountAmount(beforeAmount: Int): Int {
        var discountAmount = when (couponInformation.discountCalculationType) {
            DiscountCalculationType.FIXED -> couponInformation.value
            DiscountCalculationType.PERCENT -> floor(beforeAmount * couponInformation.value / 100.0).toInt()
        }
        discountAmount = min(discountAmount, couponInformation.maximumDiscountAmount)
        this.appliedDiscountAmount = discountAmount
        return discountAmount
    }
    
    companion object {
        fun createProductCoupon(questionId: Long, couponInformation: CouponInformation, userCoupon: UserCoupon): ProductCoupon {
            return ProductCoupon(questionId, couponInformation, userCoupon)
        }
        
        fun createDuplicableProductCoupon(questionId: Long, couponInformation: CouponInformation, userCoupon: UserCoupon): ProductCoupon {
            if (!couponInformation.isDuplicable) throw CoreException(Error.WRONG_COUPON)
            return ProductCoupon(questionId, couponInformation, userCoupon)
        }
        
        fun createPaymentCoupon(couponInformation: CouponInformation, userCoupon: UserCoupon): PaymentCoupon {
            if (couponInformation.couponType != CouponType.PAYMENT) throw CoreException(Error.WRONG_COUPON)
            return PaymentCoupon(couponInformation, userCoupon)
        }
    }
}