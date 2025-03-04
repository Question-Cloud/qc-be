package com.eager.questioncloud.application.business.payment.question.service

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentHistoryRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory
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
