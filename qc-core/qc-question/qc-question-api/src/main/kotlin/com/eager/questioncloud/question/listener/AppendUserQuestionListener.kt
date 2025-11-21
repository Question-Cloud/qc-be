package com.eager.questioncloud.question.listener

import com.eager.questioncloud.common.event.IdempotentEvent
import com.eager.questioncloud.common.event.MessageListener
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.common.event.QueueListener
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.repository.UserQuestionRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("prod", "local")
@Component
@QueueListener(queueName = "append-user-question", type = QuestionPaymentEvent::class)
class AppendUserQuestionListener(
    private val userQuestionRepository: UserQuestionRepository,
) : MessageListener<QuestionPaymentEvent> {
    
    @IdempotentEvent
    override fun onMessage(event: QuestionPaymentEvent) {
        userQuestionRepository.saveAll(UserQuestion.create(event.buyerUserId, event.questionIds))
    }
}