package com.eager.questioncloud.question.dto

import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.Subject

open class QuestionInformation(
    val id: Long,
    val creatorId: Long,
    val title: String,
    val subject: Subject,
    val parentCategory: String,
    val childCategory: String,
    val thumbnail: String,
    val questionLevel: QuestionLevel,
    val price: Int,
    val promotionName: String?,
    val promotionPrice: Int?,
    val rate: Double,
)