package com.eager.questioncloud.application.api.hub.review.event

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import software.amazon.awssdk.services.sns.model.PublishRequest

class ReviewEvent(
    val eventId: String,
    val questionId: Long,
    val varianceRate: Int,
    val reviewEventType: ReviewEventType
) {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    fun toRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn("arn:aws:sns:ap-northeast-2:503561444273:question-review.fifo")
            .messageDeduplicationId(eventId)
            .messageGroupId(questionId.toString())
            .message(objectMapper.writeValueAsString(this))
            .build()
    }

    fun toJson(): String {
        return objectMapper.writeValueAsString(this)
    }
}