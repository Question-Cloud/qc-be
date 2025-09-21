package com.eager.questioncloud.payment

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory

interface QuestionPaymentHistoryRDBRepository {
    fun save(questionPaymentHistory: QuestionPaymentHistory)
    fun getQuestionPaymentHistory(userId: Long, pagingInformation: PagingInformation): List<QuestionPaymentHistory>
}