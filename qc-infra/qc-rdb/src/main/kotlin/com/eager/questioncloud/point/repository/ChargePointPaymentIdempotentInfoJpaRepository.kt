package com.eager.questioncloud.point.repository

import com.eager.questioncloud.point.entity.ChargePointPaymentIdempotentInfoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChargePointPaymentIdempotentInfoJpaRepository : JpaRepository<ChargePointPaymentIdempotentInfoEntity, String> {
}