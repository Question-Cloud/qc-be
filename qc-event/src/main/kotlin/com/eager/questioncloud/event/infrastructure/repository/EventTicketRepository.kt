package com.eager.questioncloud.event.infrastructure.repository

import com.eager.questioncloud.event.model.EventTicket

interface EventTicketRepository {
    fun getUnPublishEventTickets(): List<EventTicket>
    
    fun save(eventTicket: EventTicket): EventTicket
    
    fun publish(eventId: String)
    
    fun publish(eventIds: List<String>)
}