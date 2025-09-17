package com.eager.questioncloud.question.repository

import com.eager.questioncloud.question.entity.QuestionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QuestionJpaRepository : JpaRepository<QuestionEntity, Long> {
    fun findByIdAndCreatorId(id: Long, creatorId: Long): Optional<QuestionEntity>
}
