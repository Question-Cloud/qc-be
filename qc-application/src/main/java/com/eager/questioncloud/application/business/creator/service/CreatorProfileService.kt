package com.eager.questioncloud.application.business.creator.service

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.question.enums.Subject
import org.springframework.stereotype.Service

@Service
class CreatorProfileService(
    private val creatorRepository: CreatorRepository
) {
    fun updateCreatorProfile(creator: Creator, mainSubject: Subject, introduction: String) {
        creator.updateProfile(mainSubject, introduction)
        creatorRepository.save(creator)
    }
}
