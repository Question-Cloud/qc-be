package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory

interface QuestionPaymentHistoryRepository {
    fun getQuestionPaymentHistory(userId: Long, pagingInformation: PagingInformation): List<QuestionPaymentHistory>

    fun count(userId: Long): Int

    fun save(questionPaymentHistory: QuestionPaymentHistory): QuestionPaymentHistory
}
