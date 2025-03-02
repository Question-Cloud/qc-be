package com.eager.questioncloud.core.domain.cart.infrastructure.repository

import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail
import com.eager.questioncloud.core.domain.cart.model.CartItem

interface CartItemRepository {
    fun save(cartItem: CartItem): CartItem

    fun findByUserId(userId: Long): List<CartItemDetail>

    fun deleteByIdInAndUserId(ids: List<Long>, userId: Long)

    fun isExistsInCart(userId: Long, questionId: Long): Boolean
}
