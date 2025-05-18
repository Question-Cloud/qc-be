package com.eager.questioncloud.core.domain.question.infrastructure.repository

import com.eager.questioncloud.core.domain.question.infrastructure.entity.QuestionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QuestionJpaRepository : JpaRepository<QuestionEntity, Long> {
    fun findByIdAndCreatorId(id: Long, creatorId: Long): Optional<QuestionEntity>
}
