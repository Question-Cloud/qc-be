package com.eager.questioncloud.creator.service

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.implement.CreatorRegister
import com.eager.questioncloud.creator.implement.CreatorStatisticsInitializer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterCreatorService(
    private val creatorRegister: CreatorRegister,
    private val creatorStatisticsInitializer: CreatorStatisticsInitializer,
) {
    @Transactional
    fun register(userId: Long, mainSubject: String, introduction: String): Creator {
        val creator = creatorRegister.register(userId, mainSubject, introduction)
        creatorStatisticsInitializer.initializeCreatorStatistics(creator.id)
        return creator
    }
}
