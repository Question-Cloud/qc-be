package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.CouponType

class PaymentCoupon(
    couponInformation: CouponInformation,
    userCoupon: UserCoupon
) : Coupon(couponInformation, userCoupon) {
    override fun isApplicable(questionPayment: QuestionPayment): Boolean {
        if (questionPayment.realAmount < couponInformation.minimumPurchaseAmount) throw CoreException(Error.WRONG_COUPON)
        
        return couponInformation.couponType == CouponType.PAYMENT
    }
    
    override fun doApply(questionPayment: QuestionPayment) {
        val discountAmount = getDiscountAmount(questionPayment.realAmount)
        questionPayment.applyDiscount(discountAmount)
        questionPayment.appliedPaymentCoupons.add(this)
    }
    
    override fun getName(): String {
        return couponInformation.title
    }
    
    override fun getSourceId(): Long {
        return userCoupon.id
    }
}