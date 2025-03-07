package com.eager.questioncloud.application.business.payment.point.implement

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent
import com.eager.questioncloud.core.domain.point.implement.UserPointManager
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class ChargePointPaymentPostProcessor(
    private val userPointManager: UserPointManager,
) {
    @SqsListener("charge-point.fifo")
    fun chargeUserPoint(@Payload event: ChargePointPaymentEvent) {
        userPointManager.chargePoint(event.userId, event.chargePointType.amount)
    }
}
