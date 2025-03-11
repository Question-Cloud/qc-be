package com.eager.questioncloud.application.business.payment.question.event

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import software.amazon.awssdk.services.sns.model.PublishRequest

class QuestionPaymentEvent(
    @JsonProperty("questionPayment")
    val questionPayment: QuestionPayment
) {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn("arn:aws:sns:ap-northeast-2:503561444273:question-payment-sns.fifo")
            .messageGroupId(questionPayment.order.orderId)
            .messageDeduplicationId(questionPayment.order.orderId)
            .message(objectMapper.writeValueAsString(this))
            .build()
    }

    fun toJson(): String {
        return objectMapper.writeValueAsString(this)
    }
}
