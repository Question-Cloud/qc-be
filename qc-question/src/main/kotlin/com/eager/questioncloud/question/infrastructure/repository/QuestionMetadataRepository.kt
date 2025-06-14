package com.eager.questioncloud.question.infrastructure.repository

import com.eager.questioncloud.question.domain.QuestionMetadata

interface QuestionMetadataRepository {
    fun save(questionMetadata: QuestionMetadata): QuestionMetadata

    fun update(questionMetadata: QuestionMetadata): QuestionMetadata
}