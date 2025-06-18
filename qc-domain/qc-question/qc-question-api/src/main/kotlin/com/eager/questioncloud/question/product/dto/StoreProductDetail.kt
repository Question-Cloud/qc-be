package com.eager.questioncloud.question.product.dto

import com.eager.questioncloud.question.dto.QuestionInformation

class StoreProductDetail(
    val questionContent: QuestionInformation,
    val creator: String,
    val isOwned: Boolean,
)
