package com.eager.questioncloud.cart.dto

class CartItemDetail(
    val id: Long,
    val questionId: Long,
    val title: String,
    val thumbnail: String,
    val creatorName: String,
    val subject: String,
    val price: Int
)
