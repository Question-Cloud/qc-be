package com.eager.questioncloud.event.repository

import com.eager.questioncloud.event.entity.EventTicketEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventTicketJpaRepository : JpaRepository<EventTicketEntity, String> {
}