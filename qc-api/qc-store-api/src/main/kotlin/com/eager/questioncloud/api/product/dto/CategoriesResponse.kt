package com.eager.questioncloud.api.product.dto

import com.eager.questioncloud.question.dto.QuestionCategoryGroupBySubject

class CategoriesResponse(
    val categories: List<QuestionCategoryGroupBySubject>
)