package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.CouponPolicy
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Component

@Component
class PaymentCouponApplier(
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository
) {
    fun apply(questionPayment: QuestionPayment, command: QuestionPaymentCommand) {
        if (command.userCouponId == null) {
            return
        }
        
        val userCoupon = userCouponRepository.getUserCoupon(command.userCouponId, command.userId)
        
        if (!userCouponRepository.use(userCoupon.id, questionPayment.order.orderId)) {
            throw CoreException(Error.FAIL_USE_COUPON)
        }
        
        val coupon = couponRepository.findById(userCoupon.couponId)
        
        questionPayment.applyPaymentCoupon(CouponPolicy(coupon, userCoupon))
    }
}