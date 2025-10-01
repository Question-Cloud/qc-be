package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.entity.CouponInformationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CouponJpaRepository : JpaRepository<CouponInformationEntity, Long> {
    fun findByCode(code: String): Optional<CouponInformationEntity>
}
