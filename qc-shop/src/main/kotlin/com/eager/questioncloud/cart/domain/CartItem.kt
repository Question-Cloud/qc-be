package com.eager.questioncloud.cart.domain

class CartItem(
    val id: Long = 0,
    val userId: Long,
    val questionId: Long
) {
    companion object {
        fun create(userId: Long, questionId: Long): CartItem {
            return CartItem(userId = userId, questionId = questionId)
        }
    }
}

