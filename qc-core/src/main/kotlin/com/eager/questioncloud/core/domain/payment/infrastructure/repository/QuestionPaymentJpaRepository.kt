package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionPaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionPaymentJpaRepository : JpaRepository<QuestionPaymentEntity, String> {
    fun countByUserId(userId: Long): Int
}
