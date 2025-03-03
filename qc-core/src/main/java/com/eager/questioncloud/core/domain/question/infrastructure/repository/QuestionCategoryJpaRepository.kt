package com.eager.questioncloud.core.domain.question.infrastructure.repository

import com.eager.questioncloud.core.domain.question.infrastructure.entity.QuestionCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionCategoryJpaRepository : JpaRepository<QuestionCategoryEntity, Long>
