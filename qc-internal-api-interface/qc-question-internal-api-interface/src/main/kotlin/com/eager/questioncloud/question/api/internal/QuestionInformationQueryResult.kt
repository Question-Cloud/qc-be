package com.eager.questioncloud.question.api.internal

class QuestionInformationQueryResult(
    val id: Long,
    val creatorId: Long,
    val title: String,
    val description: String,
    val subject: String,
    val parentCategory: String,
    val childCategory: String,
    val thumbnail: String,
    val questionLevel: String,
    val price: Int,
    val rate: Double,
)