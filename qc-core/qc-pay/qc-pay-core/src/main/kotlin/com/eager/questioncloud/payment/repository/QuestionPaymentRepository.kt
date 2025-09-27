package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.dto.QuestionPaymentData

interface QuestionPaymentRepository {
    fun save(questionPayment: QuestionPayment): QuestionPayment
    
    fun countByUserId(userId: Long): Int
    
    fun getQuestionPaymentData(orderId: String): QuestionPaymentData
}
