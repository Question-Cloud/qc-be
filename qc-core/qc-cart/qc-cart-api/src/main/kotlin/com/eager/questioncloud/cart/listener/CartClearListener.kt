package com.eager.questioncloud.cart.listener

import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.event.model.QuestionPaymentEvent
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CartClearListener(
    private val cartItemRepository: CartItemRepository
) {
    @SqsListener("clear-cart-item.fifo")
    fun clearCart(@Payload event: QuestionPaymentEvent) {
        cartItemRepository.deleteByQuestionIdInAndUserId(
            event.data.questionIds,
            event.data.buyerUserId
        )
    }
}