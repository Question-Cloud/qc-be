package com.eager.questioncloud.cart.handler

import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import org.springframework.stereotype.Component

@Component
class CartClearHandler(
    private val cartItemRepository: CartItemRepository
) {
    fun clearCart(event: QuestionPaymentEvent) {
        cartItemRepository.deleteByQuestionIdInAndUserId(
            event.questionIds,
            event.buyerUserId
        )
    }
}