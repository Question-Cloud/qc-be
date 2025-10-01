package com.eager.questioncloud.payment.coupon.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.repository.CouponInformationRepository
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponRegister(
    private val couponInformationRepository: CouponInformationRepository,
    private val userCouponRepository: UserCouponRepository,
) {
    @Transactional
    fun registerCoupon(userId: Long, couponCode: String) {
        val coupon = couponInformationRepository.findByCode(couponCode)
        
        if (userCouponRepository.isRegistered(userId, coupon.id)) {
            throw CoreException(Error.ALREADY_REGISTER_COUPON)
        }
        
        if (!couponInformationRepository.decreaseCount(coupon.id)) {
            throw CoreException(Error.LIMITED_COUPON)
        }
        
        userCouponRepository.save(UserCoupon.create(userId, coupon))
    }
}
