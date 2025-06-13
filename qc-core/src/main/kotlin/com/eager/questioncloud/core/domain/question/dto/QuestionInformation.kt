package com.eager.questioncloud.core.domain.question.dto

import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.question.enums.Subject

class QuestionInformation(
    val id: Long,
    val creatorId: Long,
    val title: String,
    val subject: Subject,
    val parentCategory: String,
    val childCategory: String,
    val thumbnail: String,
    val questionLevel: QuestionLevel,
    val price: Int,
    val rate: Double,
)