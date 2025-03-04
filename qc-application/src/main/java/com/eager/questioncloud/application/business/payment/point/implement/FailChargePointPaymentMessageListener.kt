package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.message.FailChargePointPaymentMessage
import com.eager.questioncloud.application.message.MessageSender
import com.eager.questioncloud.application.message.MessageType
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository
import com.eager.questioncloud.pg.implement.PGPaymentProcessor
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentMessageListener(
    private val chargePointPaymentRepository: ChargePointPaymentRepository,
    private val pgPaymentProcessor: PGPaymentProcessor,
    private val messageSender: MessageSender,
) {
    @RabbitListener(id = "fail-charge-point", queues = ["fail-charge-point"])
    fun failHandler(message: FailChargePointPaymentMessage) {
        try {
            val chargePointPayment = chargePointPaymentRepository.findByPaymentId(message.paymentId)
            chargePointPayment.fail()
            chargePointPaymentRepository.save(chargePointPayment)
            pgPaymentProcessor.cancel(chargePointPayment.paymentId)
        } catch (e: Exception) {
            message.increaseFailCount()
            messageSender.sendDelayMessage(MessageType.FAIL_CHARGE_POINT, message, message.failCount)
        }
    }
}
