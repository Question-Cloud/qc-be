package com.eager.questioncloud.event.listener.userquestion

import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.event.model.QuestionPaymentEvent
import com.eager.questioncloud.userquestion.domain.UserQuestion
import com.eager.questioncloud.userquestion.infrastructure.repository.UserQuestionRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class AppendUserQuestionListener(
    private val userQuestionRepository: UserQuestionRepository,
) {
    @SqsListener("append-user-question.fifo")
    @IdempotentEvent
    fun appendUserQuestion(@Payload event: QuestionPaymentEvent) {
        userQuestionRepository.saveAll(UserQuestion.create(event.data.buyerUserId, event.data.questionIds))
    }
}
