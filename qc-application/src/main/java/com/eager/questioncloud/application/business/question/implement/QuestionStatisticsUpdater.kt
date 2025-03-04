package com.eager.questioncloud.application.business.question.implement

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.payment.model.QuestionOrderItem
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class QuestionStatisticsUpdater(
    private val questionRepository: QuestionRepository
) {
    @EventListener
    fun updateSalesCount(event: QuestionPaymentEvent) {
        event.questionPayment.order.items
            .forEach(Consumer { item: QuestionOrderItem -> questionRepository.increaseQuestionCount(item.questionId) })
    }
}
