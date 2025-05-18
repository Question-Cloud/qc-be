package com.eager.questioncloud.core.domain.question.infrastructure.repository

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.MainQuestionCategory

interface QuestionCategoryRepository {
    fun getMainQuestionCategories(): List<MainQuestionCategory>
}
