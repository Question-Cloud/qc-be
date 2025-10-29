package com.eager.questioncloud.creator.api.internal

import com.eager.questioncloud.creator.repository.CreatorRepository
import org.springframework.stereotype.Component

@Component
class CreatorCommandAPIImpl(
    private val creatorRepository: CreatorRepository,
) : CreatorCommandAPI {
    override fun updateCreatorProfile(creatorId: Long, mainSubject: String, introduction: String) {
        val creator = creatorRepository.findById(creatorId)
        creator.updateProfile(mainSubject, introduction)
        creatorRepository.save(creator)
    }
}