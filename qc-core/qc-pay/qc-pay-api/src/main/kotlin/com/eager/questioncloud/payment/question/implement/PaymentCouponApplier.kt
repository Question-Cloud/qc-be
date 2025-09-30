package com.eager.questioncloud.payment.question.implement

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
        if (command.paymentUserCouponId == null) {
            return
        }
        
        val userCoupon = userCouponRepository.getUserCoupon(command.paymentUserCouponId, command.userId)
        val coupon = couponRepository.findById(userCoupon.couponId)
        userCouponRepository.use(userCoupon.id, questionPayment.order.orderId)
        
        questionPayment.applyPaymentCoupon(CouponPolicy(coupon, userCoupon))
    }
}