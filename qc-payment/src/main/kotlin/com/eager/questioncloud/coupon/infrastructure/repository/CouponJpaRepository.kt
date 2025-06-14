package com.eager.questioncloud.coupon.infrastructure.repository

import com.eager.questioncloud.coupon.infrastructure.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CouponJpaRepository : JpaRepository<CouponEntity, Long> {
    fun findByCode(code: String): Optional<CouponEntity>
}
