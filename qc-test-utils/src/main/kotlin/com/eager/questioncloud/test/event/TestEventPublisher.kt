package com.eager.questioncloud.test.event

import com.eager.questioncloud.common.event.Event
import com.eager.questioncloud.common.event.EventPublisher
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("test")
@Component
class TestEventPublisher : EventPublisher {
    override fun publish(event: Event) {
    }
}