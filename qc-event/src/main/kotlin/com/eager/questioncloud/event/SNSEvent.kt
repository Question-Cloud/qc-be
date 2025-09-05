package com.eager.questioncloud.event

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import software.amazon.awssdk.services.sns.model.PublishRequest

interface SNSEvent {
    val eventId: String
    
    fun toRequest(): PublishRequest
    
    fun toBatchRequestEntry(): PublishBatchRequestEntry
    
    fun toJson(): String {
        return objectMapper.writeValueAsString(this)
    }
    
    fun getTopicArn(): String
    
    companion object {
        val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}