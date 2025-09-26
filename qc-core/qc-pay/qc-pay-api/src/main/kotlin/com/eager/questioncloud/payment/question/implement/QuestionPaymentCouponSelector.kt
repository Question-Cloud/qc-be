package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Component

@Component
class QuestionPaymentCouponSelector(
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
) {
    fun select(userCouponId: Long?, userId: Long): QuestionPaymentCoupon {
        if (userCouponId == null) {
            return QuestionPaymentCoupon.noDiscount()
        }
        
        val userCoupon = userCouponRepository.getUserCoupon(userCouponId, userId)
        userCoupon.validate()
        
        if (!userCouponRepository.use(userCoupon.id)) {
            throw CoreException(Error.FAIL_USE_COUPON)
        }
        
        val coupon = couponRepository.findById(userCoupon.couponId)
        
        return QuestionPaymentCoupon.create(coupon, userCoupon)
    }
}
