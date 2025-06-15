package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.payment.domain.QuestionPayment

interface QuestionPaymentRepository {
    fun save(questionPayment: QuestionPayment): QuestionPayment

    fun countByUserId(userId: Long): Int
}
