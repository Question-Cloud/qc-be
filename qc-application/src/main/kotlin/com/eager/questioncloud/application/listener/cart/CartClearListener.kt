package com.eager.questioncloud.application.listener.cart

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.cart.infrastructure.repository.CartItemRepository
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
            event.questionPayment.order.questionIds,
            event.questionPayment.userId
        )
    }
}