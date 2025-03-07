package com.eager.questioncloud.application.business.payment.point.service

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent
import com.eager.questioncloud.application.business.payment.point.implement.ChargePointPaymentApprover
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.pg.implement.PGPaymentProcessor
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sns.SnsClient

@Component
class ChargePointPaymentService(
    private val chargePointPaymentApprover: ChargePointPaymentApprover,
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val pgPaymentProcessor: PGPaymentProcessor,
    private val snsClient: SnsClient,
) {
    fun createOrder(chargePointPayment: ChargePointPayment) {
        chargePointPaymentRepository.save(chargePointPayment)
    }

    fun approvePayment(paymentId: String) {
        val pgPayment = pgPaymentProcessor.getPayment(paymentId)
        val chargePointPayment = chargePointPaymentApprover.approve(pgPayment)
        snsClient.publish(ChargePointPaymentEvent.from(chargePointPayment).toRequest())
    }

    fun isCompletePayment(userId: Long, paymentId: String): Boolean {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId)
    }
}
