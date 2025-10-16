package com.eager.questioncloud.cart.service

import com.eager.questioncloud.cart.dto.CartItemDetail
import com.eager.questioncloud.cart.implement.CartItemAppender
import com.eager.questioncloud.cart.implement.CartItemDetailReader
import com.eager.questioncloud.cart.implement.CartItemRemover
import com.eager.questioncloud.cart.implement.CartItemValidator
import org.springframework.stereotype.Component

@Component
class CartService(
    private val cartItemValidator: CartItemValidator,
    private val cartItemAppender: CartItemAppender,
    private val cartItemDetailReader: CartItemDetailReader,
    private val cartItemRemover: CartItemRemover,
) {
    fun getCartItemDetails(userId: Long): List<CartItemDetail> {
        return cartItemDetailReader.getCartItemDetails(userId)
    }
    
    fun appendCartItem(userId: Long, questionId: Long) {
        cartItemValidator.validate(userId, questionId)
        cartItemAppender.append(userId, questionId)
    }
    
    fun removeCartItem(ids: List<Long>, userId: Long) {
        cartItemRemover.removeCartItem(ids, userId)
    }
}
