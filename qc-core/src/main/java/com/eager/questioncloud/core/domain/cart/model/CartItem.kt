package com.eager.questioncloud.core.domain.cart.model

class CartItem(
    val id: Long? = null,
    val userId: Long,
    val questionId: Long
) {
    companion object {
        @JvmStatic
        fun create(userId: Long, questionId: Long): CartItem {
            return CartItem(userId = userId, questionId = questionId)
        }
    }
}

