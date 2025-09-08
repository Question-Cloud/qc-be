package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.dto.AvailableUserCoupon

interface UserCouponRepository {
    fun getUserCoupon(userCouponId: Long, userId: Long): UserCoupon
    
    fun getUserCoupon(userCouponId: Long): UserCoupon
    
    fun isRegistered(userId: Long, couponId: Long): Boolean
    
    fun save(userCoupon: UserCoupon): UserCoupon
    
    fun getAvailableUserCoupons(userId: Long): List<AvailableUserCoupon>
    
    fun use(userCouponId: Long): Boolean
    
    fun deleteAllInBatch()
}
