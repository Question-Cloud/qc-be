package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.payment.infrastructure.entity.QuestionPaymentEventLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionPaymentEventLogJpaRepository : JpaRepository<QuestionPaymentEventLogEntity, String> {
}