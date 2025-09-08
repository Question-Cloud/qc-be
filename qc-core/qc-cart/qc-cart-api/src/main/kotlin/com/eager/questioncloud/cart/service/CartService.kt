package com.eager.questioncloud.cart.service

import com.eager.questioncloud.cart.dto.CartItemDetail
import com.eager.questioncloud.cart.implement.CartItemAppender
import com.eager.questioncloud.cart.implement.CartItemDetailReader
import com.eager.questioncloud.cart.repository.CartItemRepository
import org.springframework.stereotype.Component

@Component
class CartService(
    private val cartItemAppender: CartItemAppender,
    private val cartItemDetailReader: CartItemDetailReader,
    private val cartItemRepository: CartItemRepository,
) {
    fun getCartItemDetails(userId: Long): List<CartItemDetail> {
        return cartItemDetailReader.getCartItemDetails(userId)
    }
    
    fun appendCartItem(userId: Long, questionId: Long) {
        cartItemAppender.append(userId, questionId)
    }
    
    fun removeCartItem(ids: List<Long>, userId: Long) {
        cartItemRepository.deleteByIdInAndUserId(ids, userId)
    }
}
