package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.payment.domain.QuestionPayment
import org.springframework.stereotype.Component

@Component
class QuestionPaymentCouponProcessor(
    private val userCouponRepository: UserCouponRepository
) {
    fun applyCoupon(questionPayment: QuestionPayment) {
        if (!questionPayment.isUsedCoupon()) {
            return
        }

        questionPayment.applyCoupon()

        if (!userCouponRepository.use(questionPayment.questionPaymentCoupon!!.userCouponId)) {
            throw CoreException(Error.FAIL_USE_COUPON)
        }
    }
}
