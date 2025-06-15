package com.eager.questioncloud.payment.coupon.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.coupon.domain.UserCoupon
import com.eager.questioncloud.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.coupon.infrastructure.repository.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponRegister(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
) {
    @Transactional
    fun registerCoupon(userId: Long, couponCode: String) {
        val coupon = couponRepository.findByCode(couponCode)

        if (userCouponRepository.isRegistered(userId, coupon.id)) {
            throw CoreException(Error.ALREADY_REGISTER_COUPON)
        }

        if (!couponRepository.decreaseCount(coupon.id)) {
            throw CoreException(Error.LIMITED_COUPON)
        }

        userCouponRepository.save(UserCoupon.create(userId, coupon))
    }
}
