package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.question.implement.CouponPolicyApplier
import com.eager.questioncloud.payment.question.implement.QuestionOrderGenerator
import com.eager.questioncloud.payment.question.implement.QuestionPaymentRecorder
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuestionPaymentService(
    private val questionOrderGenerator: QuestionOrderGenerator,
    private val couponPolicyApplier: CouponPolicyApplier,
    private val questionPaymentRecorder: QuestionPaymentRecorder,
    private val pointCommandAPI: PointCommandAPI,
    private val eventPublisher: EventPublisher,
) {
    @Transactional
    fun payment(command: QuestionPaymentCommand): QuestionPayment {
        val order = questionOrderGenerator.generateQuestionOrder(command.userId, command.questionIds)
        val questionPayment = QuestionPayment.create(command.userId, order)
        
        couponPolicyApplier.apply(questionPayment, command)
        pointCommandAPI.usePoint(questionPayment.userId, questionPayment.realAmount)
        questionPaymentRecorder.record(questionPayment)
        
        eventPublisher.publish(toEvent(questionPayment))
        return questionPayment
    }
    
    private fun toEvent(questionPayment: QuestionPayment): QuestionPaymentEvent {
        return QuestionPaymentEvent(
            questionPayment.order.orderId,
            questionPayment.userId,
            questionPayment.order.questionIds,
            questionPayment.realAmount,
        )
    }
}
