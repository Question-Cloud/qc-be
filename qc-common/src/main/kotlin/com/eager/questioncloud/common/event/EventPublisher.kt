package com.eager.questioncloud.common.event

interface EventPublisher {
    fun publish(event: Event)
}