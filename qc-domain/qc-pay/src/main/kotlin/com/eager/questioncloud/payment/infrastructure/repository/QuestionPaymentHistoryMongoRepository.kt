package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.payment.infrastructure.document.QuestionPaymentHistoryDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface QuestionPaymentHistoryMongoRepository :
    MongoRepository<QuestionPaymentHistoryDocument, Long> {
    fun findByUserId(
        userId: Long,
        pageable: Pageable
    ): List<QuestionPaymentHistoryDocument>

    fun countByUserId(userId: Long): Int
}
