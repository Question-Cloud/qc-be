package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent
import com.eager.questioncloud.application.message.FailChargePointPaymentMessage
import com.eager.questioncloud.application.message.MessageSender
import com.eager.questioncloud.application.message.MessageType
import com.eager.questioncloud.core.domain.point.implement.UserPointManager
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentPostProcessor(
    private val userPointManager: UserPointManager,
    private val messageSender: MessageSender,
) {
    @EventListener
    fun chargeUserPoint(event: ChargePointPaymentEvent) {
        try {
            userPointManager.chargePoint(event.userId, event.chargePointType.amount)
        } catch (e: Exception) {
            messageSender.sendMessage(
                MessageType.FAIL_CHARGE_POINT,
                FailChargePointPaymentMessage.create(event.paymentId)
            )
            throw CoreException(Error.PAYMENT_ERROR)
        }
    }
}
