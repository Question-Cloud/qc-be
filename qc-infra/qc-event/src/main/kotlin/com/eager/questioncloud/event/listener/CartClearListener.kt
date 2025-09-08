package com.eager.questioncloud.event.listener

import com.eager.questioncloud.cart.handler.CartClearHandler
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CartClearListener(
    private val cartClearHandler: CartClearHandler
) {
    @SqsListener("clear-cart-item.fifo")
    fun handle(@Payload event: QuestionPaymentEvent) {
        cartClearHandler.clearCart(event)
    }
}