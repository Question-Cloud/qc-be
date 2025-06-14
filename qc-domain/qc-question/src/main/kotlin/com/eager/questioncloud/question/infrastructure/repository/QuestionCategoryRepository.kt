package com.eager.questioncloud.question.infrastructure.repository

import com.eager.questioncloud.question.dto.QuestionCategoryGroupBySubject

interface QuestionCategoryRepository {
    fun getMainQuestionCategories(): List<QuestionCategoryGroupBySubject.MainQuestionCategory>
}
