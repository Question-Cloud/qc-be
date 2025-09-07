package com.eager.questioncloud.event.implement

import com.eager.questioncloud.common.exception.ExceptionSlackNotifier
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sns.SnsAsyncClient
import java.util.*

@Component
class TestEventPublisher(
    private val snsAsyncClient: SnsAsyncClient,
    private val slackNotifier: ExceptionSlackNotifier,
) : EventPublisher<TestEvent>(snsAsyncClient, slackNotifier) {
    private var isSend: Boolean = false
    
    override fun saveEventTicket(event: TestEvent) {
    }
    
    override fun publishEvent(event: TestEvent) {
    }
    
    override fun getUnpublishedEvents(): List<TestEvent> {
        if (isSend) {
            return emptyList()
        }
        
        val list = mutableListOf<TestEvent>()
        
        for (i in 1..30) {
            list.add(TestEvent(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
        }
        
        isSend = true
        
        return list
    }
    
    override fun updateRepublishStatus(eventIds: List<String>) {
    }
}