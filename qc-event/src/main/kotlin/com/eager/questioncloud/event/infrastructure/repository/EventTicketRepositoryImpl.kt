package com.eager.questioncloud.event.infrastructure.repository

import com.eager.questioncloud.event.infrastructure.entity.EventTicketEntity
import com.eager.questioncloud.event.infrastructure.entity.QEventTicketEntity.eventTicketEntity
import com.eager.questioncloud.event.model.EventTicket
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class EventTicketRepositoryImpl(
    private val eventTicketJpaRepository: EventTicketJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : EventTicketRepository {
    override fun save(eventTicket: EventTicket): EventTicket {
        return eventTicketJpaRepository.save(EventTicketEntity.createNewEntity(eventTicket)).toModel()
    }
    
    @Transactional
    override fun publish(eventId: String) {
        jpaQueryFactory.update(eventTicketEntity)
            .set(eventTicketEntity.isPublish, true)
            .set(eventTicketEntity.publishedAt, LocalDateTime.now())
            .where(eventTicketEntity.eventId.eq(eventId))
            .execute()
    }
}