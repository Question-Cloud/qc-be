package com.eager.questioncloud.common.event

import io.hypersistence.tsid.TSID

class Event(
    val eventId: String,
    val eventType: EventType,
    val payload: Any
) {
    companion object {
        fun create(eventType: EventType, payload: Any): Event {
            return Event(TSID.Factory.getTsid().toString(), eventType, payload)
        }
    }
}