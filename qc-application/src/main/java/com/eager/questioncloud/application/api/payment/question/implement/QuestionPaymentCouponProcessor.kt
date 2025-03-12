package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Component

@Component
class QuestionPaymentCouponProcessor(
    private val userCouponRepository: UserCouponRepository
) {
    fun applyCoupon(questionPayment: QuestionPayment) {
        if (!questionPayment.isUsingCoupon) {
            return
        }

        questionPayment.applyCoupon()

        if (!userCouponRepository.use(questionPayment.questionPaymentCoupon!!.userCouponId)) {
            throw CoreException(Error.FAIL_USE_COUPON)
        }
    }
}
