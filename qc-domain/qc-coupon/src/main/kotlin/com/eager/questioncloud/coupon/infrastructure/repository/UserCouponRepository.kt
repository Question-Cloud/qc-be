package com.eager.questioncloud.coupon.infrastructure.repository

import com.eager.questioncloud.coupon.domain.UserCoupon
import com.eager.questioncloud.coupon.dto.AvailableUserCoupon

interface UserCouponRepository {
    fun getUserCoupon(userCouponId: Long, userId: Long): UserCoupon

    fun getUserCoupon(userCouponId: Long): UserCoupon

    fun isRegistered(userId: Long, couponId: Long): Boolean

    fun save(userCoupon: UserCoupon): UserCoupon

    fun getAvailableUserCoupons(userId: Long): List<AvailableUserCoupon>

    fun use(userCouponId: Long): Boolean

    fun deleteAllInBatch()
}
