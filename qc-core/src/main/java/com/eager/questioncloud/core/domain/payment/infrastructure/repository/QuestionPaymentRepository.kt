package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment

interface QuestionPaymentRepository {
    fun save(questionPayment: QuestionPayment): QuestionPayment

    fun countByUserId(userId: Long): Int
}
