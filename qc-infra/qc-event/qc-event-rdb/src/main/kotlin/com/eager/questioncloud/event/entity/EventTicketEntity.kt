package com.eager.questioncloud.event.entity

import com.eager.questioncloud.common.entity.BaseCustomIdEntity
import com.eager.questioncloud.common.event.EventType
import com.eager.questioncloud.event.model.EventTicket
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "event_ticket")
class EventTicketEntity(
    @Id val eventId: String,
    @Column val isPublish: Boolean,
    @Enumerated(EnumType.STRING) @Column val eventType: EventType,
    @Column val topicArn: String,
    @Column val payload: String,
    @Column val createdAt: LocalDateTime,
    @Column var publishedAt: LocalDateTime?,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    companion object {
        fun createNewEntity(eventTicket: EventTicket): EventTicketEntity {
            return EventTicketEntity(
                eventTicket.eventId,
                false,
                eventTicket.eventType,
                eventTicket.topicArn,
                eventTicket.payload,
                LocalDateTime.now(),
                null,
                true
            )
        }
    }
    
    override fun getId(): String {
        return eventId
    }
    
    fun toModel(): EventTicket {
        return EventTicket(eventId, isPublish, eventType, topicArn, payload, createdAt, publishedAt)
    }
}