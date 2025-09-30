package com.eager.questioncloud.payment.dto

data class QuestionInfo(
    val questionId: Long,
    val creatorId: Long,
    val title: String,
    val price: Int,
)
