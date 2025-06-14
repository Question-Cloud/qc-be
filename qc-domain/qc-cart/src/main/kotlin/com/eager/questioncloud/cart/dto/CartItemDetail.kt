package com.eager.questioncloud.cart.dto

import com.eager.questioncloud.question.enums.Subject

class CartItemDetail(
    val id: Long,
    val questionId: Long,
    val title: String,
    val thumbnail: String,
    val creatorName: String,
    val subject: Subject,
    val price: Int
)
