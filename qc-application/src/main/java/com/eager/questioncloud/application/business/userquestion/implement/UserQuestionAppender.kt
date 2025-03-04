package com.eager.questioncloud.application.business.userquestion.implement

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.application.message.FailQuestionPaymentMessage
import com.eager.questioncloud.application.message.MessageSender
import com.eager.questioncloud.application.message.MessageType
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion.Companion.create
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class UserQuestionAppender(
    private val userQuestionRepository: UserQuestionRepository,
    private val messageSender: MessageSender,
) {
    @EventListener
    fun appendUserQuestion(event: QuestionPaymentEvent) {
        try {
            userQuestionRepository.saveAll(
                create(event.questionPayment.userId, event.questionPayment.order.questionIds)
            )
        } catch (e: Exception) {
            messageSender.sendMessage(
                MessageType.FAIL_QUESTION_PAYMENT,
                FailQuestionPaymentMessage.create(event.questionPayment)
            )
            throw CoreException(Error.PAYMENT_ERROR)
        }
    }
}
