package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.repository.CartItemRepository
import org.springframework.stereotype.Component

@Component
class CartItemRemover(
    private val cartItemRepository: CartItemRepository,
) {
    fun removeCartItem(ids: List<Long>, userId: Long) {
        cartItemRepository.deleteByIdInAndUserId(ids, userId)
    }
}