package com.eager.questioncloud.question.domain

class Promotion(
    val id: Long = 0,
    val questionId: Long,
    val title: String,
    val salePrice: Int,
    val isActive: Boolean,
)