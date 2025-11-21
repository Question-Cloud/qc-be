package com.eager.questioncloud.cart.listener

import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.common.event.MessageListener
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.common.event.QueueListener
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("prod", "local")
@Component
@QueueListener(queueName = "clear-cart-item", type = QuestionPaymentEvent::class)
class CartClearListener(
    private val cartItemRepository: CartItemRepository
) : MessageListener<QuestionPaymentEvent> {
    override fun onMessage(event: QuestionPaymentEvent) {
        cartItemRepository.deleteByQuestionIdInAndUserId(
            event.questionIds,
            event.buyerUserId
        )
    }
}