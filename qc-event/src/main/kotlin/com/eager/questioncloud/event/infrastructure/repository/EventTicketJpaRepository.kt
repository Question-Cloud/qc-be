package com.eager.questioncloud.event.infrastructure.repository

import com.eager.questioncloud.event.infrastructure.entity.EventTicketEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventTicketJpaRepository : JpaRepository<EventTicketEntity, String> {
}