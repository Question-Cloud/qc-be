package com.eager.questioncloud.question.handler

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdateQuestionSalesCountHandler(
    private val questionMetadataRepository: QuestionMetadataRepository,
) {
    @Transactional
    fun updateSalesCount(event: QuestionPaymentEvent) {
        event.questionIds.forEach { questionMetadataRepository.increaseSales(it) }
    }
}
