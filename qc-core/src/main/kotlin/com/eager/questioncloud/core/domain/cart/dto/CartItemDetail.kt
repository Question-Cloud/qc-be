package com.eager.questioncloud.core.domain.cart.dto

import com.eager.questioncloud.core.domain.question.enums.Subject

class CartItemDetail(
    val id: Long,
    val questionId: Long,
    val title: String,
    val thumbnail: String,
    val creatorName: String,
    val subject: Subject,
    val price: Int
)
