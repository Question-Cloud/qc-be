package com.eager.questioncloud.core.domain.creator.infrastructure.entity

import com.eager.questioncloud.core.domain.creator.model.Creator
import jakarta.persistence.*

@Entity
@Table(name = "creator")
class CreatorEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long?,
    @Column var userId: Long,
    @Embedded var creatorProfileEntity: CreatorProfileEntity
) {
    fun toModel(): Creator {
        return Creator(id, userId, creatorProfileEntity.toModel())
    }

    companion object {
        @JvmStatic
        fun from(creator: Creator): CreatorEntity {
            return CreatorEntity(
                creator.id,
                creator.userId,
                CreatorProfileEntity.from(creator.creatorProfile)
            )
        }
    }
}
