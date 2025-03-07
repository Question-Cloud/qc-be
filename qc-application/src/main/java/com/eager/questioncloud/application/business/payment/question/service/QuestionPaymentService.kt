package com.eager.questioncloud.application.business.payment.question.service

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.application.business.payment.question.implement.QuestionPaymentProcessor
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment.Companion.create
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sns.SnsClient

@Service
class QuestionPaymentService(
    private val questionPaymentProcessor: QuestionPaymentProcessor,
    private val snsClient: SnsClient,
) {
    fun payment(userId: Long, order: QuestionOrder, questionPaymentCoupon: QuestionPaymentCoupon?): QuestionPayment {
        val questionPayment = create(userId, questionPaymentCoupon, order)
        questionPaymentProcessor.payment(questionPayment)
        snsClient.publish(QuestionPaymentEvent(questionPayment).toRequest())
        return questionPayment
    }
}
