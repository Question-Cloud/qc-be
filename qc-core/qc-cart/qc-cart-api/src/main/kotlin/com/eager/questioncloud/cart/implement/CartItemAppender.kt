package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import org.springframework.stereotype.Component

@Component
class CartItemAppender(
    private val cartItemRepository: CartItemRepository,
) {
    fun append(userId: Long, questionId: Long) {
        cartItemRepository.save(CartItem.create(userId, questionId))
    }
}
