package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.QuestionOrder

interface QuestionOrderRepository {
    fun save(questionOrder: QuestionOrder)
    
    fun deleteAllInBatch()
}
