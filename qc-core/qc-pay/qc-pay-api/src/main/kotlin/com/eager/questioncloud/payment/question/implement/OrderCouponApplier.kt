package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.domain.CouponPolicy
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.question.command.QuestionOrderCommand
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
        val allUserCouponIds = command.allUserCouponIds
        if (allUserCouponIds.isEmpty()) return
        
        val userCoupons = userCouponRepository.getUserCoupon(allUserCouponIds, command.userId)
        val coupons = couponRepository.findByIdIn(userCoupons.map { it.couponId })
        
        val userCouponMap = userCoupons.associateBy { it.id }
        val couponMap = coupons.associateBy { it.id }
        
        command.orders.forEach { orderCommand ->
            applyOrderCoupon(questionOrder, orderCommand, userCouponMap, couponMap)
            applyDuplicableCoupon(questionOrder, orderCommand, userCouponMap, couponMap)
        }
    }
    
    private fun applyOrderCoupon(
        questionOrder: QuestionOrder,
        orderCommand: QuestionOrderCommand,
        userCouponMap: Map<Long, UserCoupon>,
        couponMap: Map<Long, Coupon>
    ) {
        orderCommand.orderUserCouponId?.let { couponId ->
            val userCoupon = userCouponMap.getValue(couponId)
            val coupon = couponMap.getValue(userCoupon.couponId)
            
            userCouponRepository.use(userCoupon.id, questionOrder.orderId)
            questionOrder.applyOrderCoupon(orderCommand.questionId, CouponPolicy(coupon, userCoupon))
        }
    }
    
    private fun applyDuplicableCoupon(
        questionOrder: QuestionOrder,
        orderCommand: QuestionOrderCommand,
        userCouponMap: Map<Long, UserCoupon>,
        couponMap: Map<Long, Coupon>
    ) {
        orderCommand.duplicableOrderUserCouponId?.let { couponId ->
            val userCoupon = userCouponMap.getValue(couponId)
            val coupon = couponMap.getValue(userCoupon.couponId)
            
            if (!coupon.isDuplicable) throw CoreException(Error.WRONG_COUPON)
            
            questionOrder.applyOrderCoupon(orderCommand.questionId, CouponPolicy(coupon, userCoupon))
            userCouponRepository.use(userCoupon.id, questionOrder.orderId)
        }
    }
}
