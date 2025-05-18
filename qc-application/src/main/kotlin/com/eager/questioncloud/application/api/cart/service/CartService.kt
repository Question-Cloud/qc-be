package com.eager.questioncloud.application.api.cart.service

import com.eager.questioncloud.application.api.cart.implement.CartItemAppender
import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail
import com.eager.questioncloud.core.domain.cart.infrastructure.repository.CartItemRepository
import org.springframework.stereotype.Component

@Component
class CartService(
    private val cartItemAppender: CartItemAppender,
    private val cartItemRepository: CartItemRepository,
) {
    fun getCartItems(userId: Long): List<CartItemDetail> {
        return cartItemRepository.findByUserId(userId)
    }

    fun appendCartItem(userId: Long, questionId: Long) {
        cartItemAppender.append(userId, questionId)
    }

    fun removeCartItem(ids: List<Long>, userId: Long) {
        cartItemRepository.deleteByIdInAndUserId(ids, userId)
    }
}
