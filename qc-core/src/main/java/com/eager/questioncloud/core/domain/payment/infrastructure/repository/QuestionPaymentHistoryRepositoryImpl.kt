package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.payment.infrastructure.document.QuestionPaymentHistoryDocument.Companion.from
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class QuestionPaymentHistoryRepositoryImpl(
    private val questionPaymentHistoryMongoRepository: QuestionPaymentHistoryMongoRepository
) : QuestionPaymentHistoryRepository {
    override fun getQuestionPaymentHistory(
        userId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionPaymentHistory> {
        return questionPaymentHistoryMongoRepository.findByUserId(
            userId,
            PageRequest.of(pagingInformation.page, pagingInformation.size, Sort.by(Sort.Order.desc("paymentId")))
        )
            .stream()
            .map { document -> document.toModel() }
            .collect(Collectors.toList())
    }

    override fun count(userId: Long): Int {
        return questionPaymentHistoryMongoRepository.countByUserId(userId)
    }

    override fun save(questionPaymentHistory: QuestionPaymentHistory): QuestionPaymentHistory {
        return questionPaymentHistoryMongoRepository.save(from(questionPaymentHistory)).toModel()
    }
}
