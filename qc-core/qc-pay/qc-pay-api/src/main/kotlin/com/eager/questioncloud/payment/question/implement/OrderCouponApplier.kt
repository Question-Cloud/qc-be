package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.CouponPolicy
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Component

@Component
class OrderCouponApplier(
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
) {
    fun apply(questionOrder: QuestionOrder, command: QuestionPaymentCommand) {
        command.orders.forEach { order ->
            if (order.orderUserCouponIds.isEmpty()) return@forEach
            
            val userCoupons = userCouponRepository.getUserCoupon(order.orderUserCouponIds, command.userId)
            val couponMap = couponRepository.findByIdIn(userCoupons.map { it.couponId }).associateBy { it.id }
            
            userCoupons.forEach { userCoupon ->
                val couponPolicy = CouponPolicy(couponMap.getValue(userCoupon.couponId), userCoupon)
                questionOrder.applyOrderCoupon(order.questionId, couponPolicy)
                if (!userCouponRepository.use(userCoupon.id, questionOrder.orderId)) {
                    throw CoreException(Error.FAIL_USE_COUPON)
                }
            }
        }
    }
}
