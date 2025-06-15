package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.infrastructure.repository.QuestionPaymentHistoryRepository
import org.springframework.stereotype.Service

@Service
class QuestionPaymentHistoryService(
    private val questionPaymentHistoryRepository: QuestionPaymentHistoryRepository
) {
    fun getQuestionPaymentHistory(userId: Long, pagingInformation: PagingInformation): List<QuestionPaymentHistory> {
        return questionPaymentHistoryRepository.getQuestionPaymentHistory(userId, pagingInformation)
    }

    fun countQuestionPaymentHistory(userId: Long): Int {
        return questionPaymentHistoryRepository.count(userId)
    }
}
