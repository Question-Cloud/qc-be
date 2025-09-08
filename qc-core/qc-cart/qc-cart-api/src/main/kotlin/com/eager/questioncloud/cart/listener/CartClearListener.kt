package com.eager.questioncloud.cart.listener

import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.common.event.QuestionPaymentEventPayload
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CartClearListener(
    private val cartItemRepository: CartItemRepository
) {
    @SqsListener("clear-cart-item.fifo")
    fun clearCart(@Payload event: QuestionPaymentEventPayload) {
        cartItemRepository.deleteByQuestionIdInAndUserId(
            event.questionIds,
            event.buyerUserId
        )
    }
}