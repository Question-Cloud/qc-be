package com.eager.questioncloud.event.infrastructure.entity

import com.eager.questioncloud.entity.BaseCustomIdEntity
import com.eager.questioncloud.event.model.EventTicket
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "event_ticket")
class EventTicketEntity(
    @Id val eventId: String,
    @Column val isPublish: Boolean,
    @Column val eventType: String,
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
        return EventTicket(eventId, isPublish, eventType, payload, createdAt, publishedAt)
    }
}