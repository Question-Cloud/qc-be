package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.repository.CouponInformationRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Component

@Component
class PaymentCouponApplier(
    private val userCouponRepository: UserCouponRepository,
    private val couponInformationRepository: CouponInformationRepository
) {
    fun apply(questionPayment: QuestionPayment, command: QuestionPaymentCommand) {
        if (command.paymentUserCouponId == null) {
            return
        }
        
        val userCoupon = userCouponRepository.getUserCoupon(command.paymentUserCouponId, command.userId)
        val couponInformation = couponInformationRepository.findById(userCoupon.couponId)
        
        val coupon = Coupon.createPaymentCoupon(couponInformation, userCoupon)
        coupon.apply(questionPayment)
        
        userCouponRepository.use(userCoupon.id, questionPayment.order.orderId)
    }
}