package com.eager.questioncloud.question.infrastructure.repository

import com.eager.questioncloud.question.infrastructure.entity.QuestionCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionCategoryJpaRepository : JpaRepository<QuestionCategoryEntity, Long>
