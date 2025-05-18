package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionPaymentEventLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionPaymentEventLogJpaRepository : JpaRepository<QuestionPaymentEventLogEntity, String> {
}