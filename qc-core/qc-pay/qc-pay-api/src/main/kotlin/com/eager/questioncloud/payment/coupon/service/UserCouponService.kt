package com.eager.questioncloud.payment.coupon.service

import com.eager.questioncloud.lock.LockKeyGenerator.generateRegisterCouponKey
import com.eager.questioncloud.lock.LockManager
import com.eager.questioncloud.payment.coupon.implement.UserCouponRegister
import com.eager.questioncloud.payment.dto.AvailableUserCoupon
import com.eager.questioncloud.payment.repository.UserCouponRepository
import org.springframework.stereotype.Service

@Service
class UserCouponService(
    private val userCouponRegister: UserCouponRegister,
    private val userCouponRepository: UserCouponRepository,
    private val lockManager: LockManager,
) {
    fun registerCoupon(userId: Long, couponCode: String) {
        lockManager.executeWithLock(
            generateRegisterCouponKey(userId, couponCode)
        ) { userCouponRegister.registerCoupon(userId, couponCode) }
    }
    
    fun getAvailableUserCoupons(userId: Long): List<AvailableUserCoupon> {
        return userCouponRepository.getAvailableUserCoupons(userId)
    }
}
