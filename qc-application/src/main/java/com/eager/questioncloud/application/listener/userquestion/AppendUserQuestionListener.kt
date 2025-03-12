package com.eager.questioncloud.application.listener.userquestion

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion.Companion.create
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class AppendUserQuestionListener(
    private val userQuestionRepository: UserQuestionRepository,
) {
    @SqsListener("append-user-question.fifo")
    fun appendUserQuestion(@Payload event: QuestionPaymentEvent) {
        userQuestionRepository.saveAll(create(event.questionPayment.userId, event.questionPayment.order.questionIds))
    }
}
