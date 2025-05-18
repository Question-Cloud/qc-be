package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory

interface QuestionPaymentHistoryRepository {
    fun getQuestionPaymentHistory(userId: Long, pagingInformation: PagingInformation): List<QuestionPaymentHistory>

    fun count(userId: Long): Int

    fun save(questionPaymentHistory: QuestionPaymentHistory): QuestionPaymentHistory
}
