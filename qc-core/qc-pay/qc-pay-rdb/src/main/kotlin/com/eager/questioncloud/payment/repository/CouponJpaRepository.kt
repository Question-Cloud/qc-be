package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CouponJpaRepository : JpaRepository<CouponEntity, Long> {
    fun findByCode(code: String): Optional<CouponEntity>
}
