package com.eager.questioncloud.application.api.hub.question.dto

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject

class QuestionCategoriesResponse(
    val categories: List<QuestionCategoryGroupBySubject>
)