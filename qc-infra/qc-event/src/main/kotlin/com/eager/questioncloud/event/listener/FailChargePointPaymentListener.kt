package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.message.FailChargePointPaymentMessagePayload
import com.eager.questioncloud.point.handler.FailChargePointPaymentHandler
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class FailChargePointPaymentListener(
    private val failChargePointPaymentHandler: FailChargePointPaymentHandler
) {
    @SqsListener("fail-charge-point-payment.fifo")
    fun handle(@Payload event: FailChargePointPaymentMessagePayload) {
        failChargePointPaymentHandler.failHandler(event)
    }
}