package com.eager.questioncloud.event

import com.fasterxml.jackson.databind.ObjectMapper
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import software.amazon.awssdk.services.sns.model.PublishRequest

interface SNSEvent {
    val eventId: String
    
    fun toRequest(objectMapper: ObjectMapper): PublishRequest
    
    fun toBatchRequestEntry(objectMapper: ObjectMapper): PublishBatchRequestEntry
    
    fun toJson(objectMapper: ObjectMapper): String {
        return objectMapper.writeValueAsString(this)
    }
    
    fun getTopicArn(): String
}