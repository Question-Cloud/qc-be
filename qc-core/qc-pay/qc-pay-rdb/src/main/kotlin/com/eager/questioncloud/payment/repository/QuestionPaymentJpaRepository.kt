package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.entity.QuestionPaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionPaymentJpaRepository :
    JpaRepository<QuestionPaymentEntity, Long> {
    fun countByUserId(userId: Long): Int
}
