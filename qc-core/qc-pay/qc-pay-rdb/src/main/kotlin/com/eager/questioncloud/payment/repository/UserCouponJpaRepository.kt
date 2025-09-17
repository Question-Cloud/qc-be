package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.entity.UserCouponEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserCouponJpaRepository : JpaRepository<UserCouponEntity, Long> {
    fun existsByUserIdAndCouponId(userId: Long, couponId: Long): Boolean
    
    fun findByIdAndUserIdAndIsUsedFalse(id: Long, userId: Long): Optional<UserCouponEntity>
}
