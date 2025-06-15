package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import org.springframework.stereotype.Component

@Component
class QuestionPaymentCouponReader(
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
) {
    fun getQuestionPaymentCoupon(userCouponId: Long?, userId: Long): QuestionPaymentCoupon? {
        if (userCouponId == null) {
            return null
        }

        val userCoupon = userCouponRepository.getUserCoupon(userCouponId, userId)
        userCoupon.validate()

        val coupon = couponRepository.findById(userCoupon.couponId)

        return QuestionPaymentCoupon.create(userCouponId, coupon)
    }
}
