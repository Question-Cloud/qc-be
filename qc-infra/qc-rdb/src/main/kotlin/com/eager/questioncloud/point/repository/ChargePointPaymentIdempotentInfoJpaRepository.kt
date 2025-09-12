package com.eager.questioncloud.point.repository

import com.eager.questioncloud.point.entity.ChargePointPaymentIdempotentInfoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ChargePointPaymentIdempotentInfoJpaRepository : JpaRepository<ChargePointPaymentIdempotentInfoEntity, String> {
    @Modifying
    @Query(
        value = "INSERT IGNORE INTO charge_point_payment_idempotent_info(order_id, payment_id, charge_point_payment_status, created_at) VALUES (:orderId, :paymentId, :status, NOW())",
        nativeQuery = true
    )
    fun insertIfAbsent(orderId: String, paymentId: String, status: String): Int
}