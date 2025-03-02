package com.eager.questioncloud.core.domain.creator.infrastructure.entity

import com.eager.questioncloud.core.domain.creator.model.CreatorProfile
import com.eager.questioncloud.core.domain.question.enums.Subject
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class CreatorProfileEntity private constructor(
    @Enumerated(EnumType.STRING)
    var mainSubject: Subject,
    var introduction: String
) {
    fun toModel(): CreatorProfile {
        return CreatorProfile.create(mainSubject, introduction)
    }

    companion object {
        @JvmStatic
        fun from(creatorProfile: CreatorProfile): CreatorProfileEntity {
            return CreatorProfileEntity(creatorProfile.mainSubject, creatorProfile.introduction)
        }
    }
}
