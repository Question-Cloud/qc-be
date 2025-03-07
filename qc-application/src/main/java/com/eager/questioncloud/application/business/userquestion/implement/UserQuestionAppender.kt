package com.eager.questioncloud.application.business.userquestion.implement

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion.Companion.create
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UserQuestionAppender(
    private val userQuestionRepository: UserQuestionRepository,
) {
    @SqsListener("append-user-question.fifo")
    fun appendUserQuestion(@Payload event: QuestionPaymentEvent) {
        userQuestionRepository.saveAll(create(event.questionPayment.userId, event.questionPayment.order.questionIds))
    }
}
