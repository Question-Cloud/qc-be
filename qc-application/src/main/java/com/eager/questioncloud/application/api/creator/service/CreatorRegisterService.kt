package com.eager.questioncloud.application.api.creator.service

import com.eager.questioncloud.application.api.creator.implement.CreatorRegister
import com.eager.questioncloud.application.api.creator.implement.CreatorStatisticsInitializer
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.user.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreatorRegisterService(
    private val creatorRegister: CreatorRegister,
    private val creatorStatisticsInitializer: CreatorStatisticsInitializer,
) {
    @Transactional
    fun register(user: User, mainSubject: Subject, introduction: String): Creator {
        val creator = creatorRegister.register(user, mainSubject, introduction)
        creatorStatisticsInitializer.initializeCreatorStatistics(creator.id!!)
        return creator
    }
}
