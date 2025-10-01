package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.domain.CouponInformation
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.question.command.QuestionOrderCommand
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.repository.CouponInformationRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Component

@Component
class OrderCouponApplier(
    private val userCouponRepository: UserCouponRepository,
    private val couponInformationRepository: CouponInformationRepository,
) {
    fun apply(questionPayment: QuestionPayment, command: QuestionPaymentCommand) {
        val allUserCouponIds = command.allUserCouponIds
        if (allUserCouponIds.isEmpty()) return
        
        val userCoupons = userCouponRepository.getUserCoupon(allUserCouponIds, command.userId)
        val couponInformation = couponInformationRepository.findByIdIn(userCoupons.map { it.couponId })
        
        val userCouponMap = userCoupons.associateBy { it.id }
        val couponMap = couponInformation.associateBy { it.id }
        
        command.orders.forEach { orderCommand ->
            applyOrderCoupon(questionPayment, orderCommand, userCouponMap, couponMap)
            applyDuplicableCoupon(questionPayment, orderCommand, userCouponMap, couponMap)
        }
    }
    
    private fun applyOrderCoupon(
        questionPayment: QuestionPayment,
        orderCommand: QuestionOrderCommand,
        userCouponMap: Map<Long, UserCoupon>,
        couponInformationMap: Map<Long, CouponInformation>
    ) {
        orderCommand.orderUserCouponId?.let { couponId ->
            val userCoupon = userCouponMap.getValue(couponId)
            val couponInformation = couponInformationMap.getValue(userCoupon.couponId)
            val coupon = Coupon.createProductCoupon(orderCommand.questionId, couponInformation, userCoupon)
            
            userCouponRepository.use(userCoupon.id, questionPayment.orderId)
            coupon.apply(questionPayment)
        }
    }
    
    private fun applyDuplicableCoupon(
        questionPayment: QuestionPayment,
        orderCommand: QuestionOrderCommand,
        userCouponMap: Map<Long, UserCoupon>,
        couponInformationMap: Map<Long, CouponInformation>
    ) {
        orderCommand.duplicableOrderUserCouponId?.let { couponId ->
            val userCoupon = userCouponMap.getValue(couponId)
            val couponInformation = couponInformationMap.getValue(userCoupon.couponId)
            val coupon = Coupon.createDuplicableProductCoupon(orderCommand.questionId, couponInformation, userCoupon)
            
            userCouponRepository.use(userCoupon.id, questionPayment.orderId)
            coupon.apply(questionPayment)
        }
    }
}
