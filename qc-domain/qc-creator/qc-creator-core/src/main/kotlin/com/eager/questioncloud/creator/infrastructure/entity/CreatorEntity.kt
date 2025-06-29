package com.eager.questioncloud.creator.infrastructure.entity

import com.eager.questioncloud.creator.domain.Creator
import jakarta.persistence.*

@Entity
@Table(name = "creator")
class CreatorEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
    @Column var userId: Long,
    @Column var mainSubject: String,
    @Column var introduction: String,
) {
    fun toModel(): Creator {
        return Creator(id, userId, mainSubject, introduction)
    }

    companion object {
        fun from(creator: Creator): CreatorEntity {
            return CreatorEntity(creator.id, creator.userId, creator.mainSubject, creator.introduction)
        }
    }
}
