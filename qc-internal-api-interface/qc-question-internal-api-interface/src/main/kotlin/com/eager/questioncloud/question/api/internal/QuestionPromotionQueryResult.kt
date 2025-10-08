package com.eager.questioncloud.question.api.internal

class QuestionPromotionQueryResult(
    val promotionInformation: List<QuestionPromotionQueryItem>
)

class QuestionPromotionQueryItem(
    val id: Long = 0,
    val questionId: Long,
    val title: String,
    val salePrice: Int
)