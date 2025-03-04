package com.eager.questioncloud.application.business.payment.point.service

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent
import com.eager.questioncloud.application.business.payment.point.implement.ChargePointPaymentApprover
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.pg.implement.PGPaymentProcessor
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentService(
    private val chargePointPaymentApprover: ChargePointPaymentApprover,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val pgPaymentProcessor: PGPaymentProcessor,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun createOrder(chargePointPayment: ChargePointPayment) {
        chargePointPaymentRepository.save(chargePointPayment)
    }

    fun approvePayment(paymentId: String) {
        val pgPayment = pgPaymentProcessor.getPayment(paymentId)
        val chargePointPayment = chargePointPaymentApprover.approve(pgPayment)
        applicationEventPublisher.publishEvent(ChargePointPaymentEvent.Companion.from(chargePointPayment))
    }

    fun isCompletePayment(userId: Long, paymentId: String): Boolean {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId)
    }
}
