package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.payment.infrastructure.entity.QuestionPaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionPaymentJpaRepository : JpaRepository<QuestionPaymentEntity, String> {
    fun countByUserId(userId: Long): Int
}
