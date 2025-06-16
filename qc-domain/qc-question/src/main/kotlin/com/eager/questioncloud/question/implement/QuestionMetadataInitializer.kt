package com.eager.questioncloud.question.implement

import com.eager.questioncloud.question.domain.QuestionMetadata
import com.eager.questioncloud.question.infrastructure.repository.QuestionMetadataRepository
import org.springframework.stereotype.Component

@Component
class QuestionMetadataInitializer(
    private val questionMetadataRepository: QuestionMetadataRepository
) {
    fun init(questionId: Long) {
        questionMetadataRepository.save(QuestionMetadata(questionId, 0, 0, 0, 0.0))
    }
}