package com.eager.questioncloud.common.event.infrastructure.entity

import com.eager.questioncloud.common.event.model.EventProcessLog
import com.eager.questioncloud.entity.BaseCustomIdEntity
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