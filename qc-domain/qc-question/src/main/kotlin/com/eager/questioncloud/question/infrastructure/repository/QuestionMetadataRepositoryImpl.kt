package com.eager.questioncloud.question.infrastructure.repository

import com.eager.questioncloud.question.domain.QuestionMetadata
import com.eager.questioncloud.question.infrastructure.entity.QuestionMetadataEntity
import org.springframework.stereotype.Repository

@Repository
class QuestionMetadataRepositoryImpl(
    private val questionMetadataJpaRepository: QuestionMetadataJpaRepository
) : QuestionMetadataRepository {
    override fun save(questionMetadata: QuestionMetadata): QuestionMetadata {
        return questionMetadataJpaRepository.save(QuestionMetadataEntity.createNewEntity(questionMetadata)).toModel()
    }

    override fun update(questionMetadata: QuestionMetadata): QuestionMetadata {
        return questionMetadataJpaRepository.save(QuestionMetadataEntity.fromExisting(questionMetadata)).toModel()
    }
}