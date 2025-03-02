package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.domain.payment.model.QuestionOrder

interface QuestionOrderRepository {
    fun save(questionOrder: QuestionOrder)

    fun deleteAllInBatch()
}
