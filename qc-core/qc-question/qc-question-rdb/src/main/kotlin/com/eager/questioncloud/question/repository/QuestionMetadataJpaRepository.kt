package com.eager.questioncloud.question.repository

import com.eager.questioncloud.question.entity.QuestionMetadataEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface QuestionMetadataJpaRepository : JpaRepository<QuestionMetadataEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByQuestionId(questionId: Long): QuestionMetadataEntity
}