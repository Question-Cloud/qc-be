package com.eager.questioncloud.core.domain.point.infrastructure.repository

import com.eager.questioncloud.core.domain.point.infrastructure.entity.ChargePointPaymentEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ChargePointPaymentJpaRepository : JpaRepository<ChargePointPaymentEntity, String> {
    fun findByPaymentId(paymentId: String): Optional<ChargePointPaymentEntity>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ChargePointPaymentEntity c where c.paymentId = :paymentId")
    fun findByPaymentIdWithLock(paymentId: String): Optional<ChargePointPaymentEntity>
}
