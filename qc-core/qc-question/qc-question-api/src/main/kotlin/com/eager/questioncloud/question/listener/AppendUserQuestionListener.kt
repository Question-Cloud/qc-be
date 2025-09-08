package com.eager.questioncloud.question.listener

import com.eager.questioncloud.common.event.QuestionPaymentEventPayload
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.repository.UserQuestionRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class AppendUserQuestionListener(
    private val userQuestionRepository: UserQuestionRepository,
) {
    @SqsListener("append-user-question.fifo")
    @IdempotentEvent
    fun appendUserQuestion(@Payload event: QuestionPaymentEventPayload) {
        userQuestionRepository.saveAll(UserQuestion.create(event.buyerUserId, event.questionIds))
    }
}
