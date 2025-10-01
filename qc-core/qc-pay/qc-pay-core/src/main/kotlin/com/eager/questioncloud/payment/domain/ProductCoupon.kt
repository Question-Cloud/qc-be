package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.CouponType

open class ProductCoupon(
    private val targetQuestionId: Long,
    couponInformation: CouponInformation,
    userCoupon: UserCoupon
) : Coupon(couponInformation, userCoupon) {
    override fun isApplicable(questionPayment: QuestionPayment): Boolean {
        val orderItem = questionPayment.getOrderItem(targetQuestionId)
        
        if (orderItem.priceAfterPromotion < couponInformation.minimumPurchaseAmount) throw CoreException(Error.WRONG_COUPON)
        
        return when (couponInformation.couponType) {
            CouponType.PRODUCT_TARGET -> orderItem.questionInfo.questionId == targetQuestionId
            else -> false
        }
    }
    
    override fun doApply(questionPayment: QuestionPayment) {
        val orderItem = questionPayment.getOrderItem(targetQuestionId)
        val discountAmount = getDiscountAmount(orderItem.priceAfterPromotion)
        orderItem.applyDiscount(discountAmount)
        orderItem.appliedCoupons.add(this)
    }
    
    override fun getName(): String {
        return couponInformation.title
    }
    
    override fun getSourceId(): Long {
        return userCoupon.id
    }
}