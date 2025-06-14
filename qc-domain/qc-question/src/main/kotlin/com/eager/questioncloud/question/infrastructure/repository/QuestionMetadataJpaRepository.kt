package com.eager.questioncloud.question.infrastructure.repository

import com.eager.questioncloud.question.infrastructure.entity.QuestionMetadataEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionMetadataJpaRepository : JpaRepository<QuestionMetadataEntity, Long> {
}