package com.eager.questioncloud.application.api.user.coupon.implement

import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon.Companion.create
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
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

        userCouponRepository.save(create(userId, coupon))
    }
}
