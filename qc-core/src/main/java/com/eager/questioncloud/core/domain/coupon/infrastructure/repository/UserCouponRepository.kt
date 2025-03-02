package com.eager.questioncloud.core.domain.coupon.infrastructure.repository

import com.eager.questioncloud.core.domain.coupon.dto.AvailableUserCoupon
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon

interface UserCouponRepository {
    fun getUserCoupon(userCouponId: Long, userId: Long): UserCoupon

    fun getUserCoupon(userCouponId: Long): UserCoupon

    fun isRegistered(userId: Long, couponId: Long): Boolean

    fun save(userCoupon: UserCoupon): UserCoupon

    fun getAvailableUserCoupons(userId: Long): List<AvailableUserCoupon>

    fun use(userCouponId: Long): Boolean

    fun deleteAllInBatch()
}
