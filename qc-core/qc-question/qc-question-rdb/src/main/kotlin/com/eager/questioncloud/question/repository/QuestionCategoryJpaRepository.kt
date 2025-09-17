package com.eager.questioncloud.question.repository

import com.eager.questioncloud.question.entity.QuestionCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionCategoryJpaRepository : JpaRepository<QuestionCategoryEntity, Long>
