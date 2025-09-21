package com.eager.questioncloud.payment

import org.springframework.data.jpa.repository.JpaRepository

interface QuestionPaymentHistoryRDBJpaRepository : JpaRepository<QuestionPaymentHistoryRDBEntity, String> {
}