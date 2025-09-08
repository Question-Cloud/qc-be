package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.payment.question.handler.QuestionPaymentHistoryRegisterHandler
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class QuestionPaymentHistoryRegisterListener(
    private val questionPaymentHistoryRegisterHandler: QuestionPaymentHistoryRegisterHandler
) {
    @SqsListener("question-payment-history-register.fifo")
    fun handle(@Payload event: QuestionPaymentEvent) {
        questionPaymentHistoryRegisterHandler.saveQuestionPaymentHistory(event)
    }
}