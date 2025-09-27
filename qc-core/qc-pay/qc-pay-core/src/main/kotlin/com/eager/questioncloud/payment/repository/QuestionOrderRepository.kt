package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.dto.QuestionOrderData

interface QuestionOrderRepository {
    fun save(questionOrder: QuestionOrder)
    
    fun deleteAllInBatch()
    
    fun getQuestionOrderData(orderId: String): List<QuestionOrderData>
}
