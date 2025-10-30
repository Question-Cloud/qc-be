package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.question.handler.AppendUserQuestionHandler
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.context.annotation.Profile
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Profile("prod", "local")
@Component
class AppendUserQuestionListener(
    private val appendUserQuestionHandler: AppendUserQuestionHandler,
) {
    @SqsListener("append-user-question.fifo")
    @IdempotentEvent
    fun handle(@Payload event: QuestionPaymentEvent) {
        appendUserQuestionHandler.appendUserQuestion(event)
    }
}