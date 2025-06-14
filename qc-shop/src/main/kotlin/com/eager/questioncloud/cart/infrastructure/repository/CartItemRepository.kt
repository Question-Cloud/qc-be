package com.eager.questioncloud.cart.infrastructure.repository

import com.eager.questioncloud.cart.domain.CartItem

interface CartItemRepository {
    fun save(cartItem: CartItem): CartItem

    fun findByUserId(userId: Long): List<CartItem>

    fun deleteByIdInAndUserId(ids: List<Long>, userId: Long)

    fun deleteByQuestionIdInAndUserId(questionIds: List<Long>, userId: Long)

    fun isExistsInCart(userId: Long, questionId: Long): Boolean

    fun deleteAllInBatch()
}
