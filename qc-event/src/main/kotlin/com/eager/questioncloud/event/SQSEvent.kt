package com.eager.questioncloud.event

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import software.amazon.awssdk.services.sns.model.PublishRequest

interface SQSEvent {
    val eventId: String

    fun toRequest(): PublishRequest

    fun toJson(): String {
        return objectMapper.writeValueAsString(this)
    }

    companion object {
        val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}