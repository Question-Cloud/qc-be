package com.eager.questioncloud.core.domain.creator.infrastructure.entity

import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.question.enums.Subject
import jakarta.persistence.*

@Entity
@Table(name = "creator")
class CreatorEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long?,
    @Column var userId: Long,
    @Enumerated(EnumType.STRING) @Column var mainSubject: Subject,
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
