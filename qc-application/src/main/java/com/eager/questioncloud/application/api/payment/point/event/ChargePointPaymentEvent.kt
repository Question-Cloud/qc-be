package com.eager.questioncloud.application.api.payment.point.event

import com.eager.questioncloud.core.domain.point.enums.ChargePointType
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import software.amazon.awssdk.services.sns.model.PublishRequest

class ChargePointPaymentEvent(
    val paymentId: String,
    val userId: Long,
    val chargePointType: ChargePointType
) {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn("arn:aws:sns:ap-northeast-2:503561444273:charge-point-payment.fifo")
            .messageGroupId(paymentId)
            .messageDeduplicationId(paymentId)
            .message(objectMapper.writeValueAsString(this))
            .build()
    }

    companion object {
        fun from(chargePointPayment: ChargePointPayment): ChargePointPaymentEvent {
            return ChargePointPaymentEvent(
                chargePointPayment.paymentId,
                chargePointPayment.userId,
                chargePointPayment.chargePointType
            )
        }
    }
}
