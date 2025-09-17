package com.eager.questioncloud.subscribe.entity

import com.eager.questioncloud.subscribe.domain.Subscribe
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "subscribe")
class SubscribeEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
    @Column var subscriberId: Long,
    @Column var creatorId: Long,
    @Column var createdAt: LocalDateTime
) {
    fun toModel(): Subscribe {
        return Subscribe(id, subscriberId, creatorId, createdAt)
    }
    
    companion object {
        fun from(subscribe: Subscribe): SubscribeEntity {
            return SubscribeEntity(
                subscribe.id,
                subscribe.subscriberId,
                subscribe.creatorId,
                subscribe.createdAt
            )
        }
    }
}
