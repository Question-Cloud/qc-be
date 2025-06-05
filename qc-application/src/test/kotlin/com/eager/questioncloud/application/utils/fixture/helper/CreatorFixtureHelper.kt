package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.question.enums.Subject

class CreatorFixtureHelper {
    companion object {
        fun createCreator(uid: Long, creatorRepository: CreatorRepository): Creator {
            return creatorRepository.save(Creator.create(uid, Subject.Biology, "Hello"))
        }
    }
}