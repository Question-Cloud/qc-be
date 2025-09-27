package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.*
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Component

@Component
class CouponPolicyApplier(
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
) {
    fun apply(questionPayment: QuestionPayment, command: QuestionPaymentCommand) {
        if (command.userCouponId == null) {
            return
        }
        
        val userCoupon = userCouponRepository.getUserCoupon(command.userCouponId, command.userId)
        userCoupon.validate()
        
        if (!userCouponRepository.use(userCoupon.id, questionPayment.order.orderId)) {
            throw CoreException(Error.FAIL_USE_COUPON)
        }
        
        val coupon = couponRepository.findById(userCoupon.couponId)
        
        questionPayment.applyDiscount(createCouponDiscountPolicy(coupon, userCoupon))
    }
    
    private fun createCouponDiscountPolicy(coupon: Coupon, userCoupon: UserCoupon): Discountable {
        return when (coupon.couponType) {
            CouponType.Fixed -> FixedCoupon(
                couponId = coupon.id,
                userCouponId = userCoupon.couponId,
                title = coupon.title,
                value = coupon.value
            )
            
            CouponType.Percent -> PercentCoupon(
                couponId = coupon.id,
                userCouponId = userCoupon.couponId,
                title = coupon.title,
                value = coupon.value
            )
        }
    }
}
