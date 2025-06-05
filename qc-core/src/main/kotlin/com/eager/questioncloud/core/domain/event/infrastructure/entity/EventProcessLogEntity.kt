package com.eager.questioncloud.core.domain.event.infrastructure.entity

import com.eager.questioncloud.core.common.BaseCustomIdEntity
import com.eager.questioncloud.core.domain.event.model.EventProcessLog
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "event_process_log")
class EventProcessLogEntity(
    @Id val idempotentKey: String,
    @Column val createdAt: LocalDateTime,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    companion object {
        fun createNewEntity(eventProcessLog: EventProcessLog): EventProcessLogEntity {
            return EventProcessLogEntity(eventProcessLog.idempotentKey, eventProcessLog.createdAt, true)
        }
    }

    override fun getId(): String {
        return idempotentKey
    }
}