package com.eager.questioncloud.question.repository

import com.eager.questioncloud.question.domain.QuestionMetadata

interface QuestionMetadataRepository {
    fun save(questionMetadata: QuestionMetadata): QuestionMetadata
    
    fun update(questionMetadata: QuestionMetadata): QuestionMetadata
    
    fun increaseSales(questionId: Long)
    
    fun getForUpdate(questionId: Long): QuestionMetadata
}