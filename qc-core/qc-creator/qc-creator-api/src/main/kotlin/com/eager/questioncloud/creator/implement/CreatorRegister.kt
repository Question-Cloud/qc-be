package com.eager.questioncloud.creator.implement

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.user.api.internal.UserCommandAPI
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CreatorRegister(
    private val creatorRepository: CreatorRepository,
    private val userCommandAPI: UserCommandAPI,
) {
    @Transactional
    fun register(userId: Long, mainSubject: String, introduction: String): Creator {
        //TODO 중복 등록 방지 처리
        userCommandAPI.toCreator(userId)
        return creatorRepository.save(Creator.create(userId, mainSubject, introduction))
    }
}
