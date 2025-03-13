package com.eager.questioncloud.core.domain.point.infrastructure.repository

import com.eager.questioncloud.core.domain.point.infrastructure.entity.ChargePointPaymentEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ChargePointPaymentJpaRepository : JpaRepository<ChargePointPaymentEntity, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ChargePointPaymentEntity c where c.orderId =:orderId")
    fun findByOrderIdWithLock(orderId: String): Optional<ChargePointPaymentEntity>
}
