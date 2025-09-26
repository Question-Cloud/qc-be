package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.*
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.repository.CouponRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Component

@Component
class CouponPolicyProcessor(
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
) {
    fun select(userCouponId: Long?, userId: Long): DiscountPolicy {
        if (userCouponId == null) {
            return NoDiscount()
        }
        
        val userCoupon = userCouponRepository.getUserCoupon(userCouponId, userId)
        userCoupon.validate()
        
        if (!userCouponRepository.use(userCoupon.id)) {
            throw CoreException(Error.FAIL_USE_COUPON)
        }
        
        val coupon = couponRepository.findById(userCoupon.couponId)
        
        return createCouponDiscountPolicy(coupon, userCoupon)
    }
    
    private fun createCouponDiscountPolicy(coupon: Coupon, userCoupon: UserCoupon): DiscountPolicy {
        return when (coupon.couponType) {
            CouponType.Fixed -> FixedCouponDiscount(
                couponId = coupon.id,
                userCouponId = userCoupon.couponId,
                title = coupon.title,
                value = coupon.value
            )
            
            CouponType.Percent -> PercentCouponDiscount(
                couponId = coupon.id,
                userCouponId = userCoupon.couponId,
                title = coupon.title,
                value = coupon.value
            )
        }
    }
}
