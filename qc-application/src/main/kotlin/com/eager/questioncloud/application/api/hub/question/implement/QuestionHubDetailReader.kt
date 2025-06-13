package com.eager.questioncloud.application.api.hub.question.implement

import com.eager.questioncloud.application.api.hub.question.dto.QuestionHubDetail
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import org.springframework.stereotype.Component

@Component
class QuestionHubDetailReader(
    private val questionRepository: QuestionRepository,
    private val creatorRepository: CreatorRepository,
    private val userRepository: UserRepository,
    private val userQuestionRepository: UserQuestionRepository
) {
    fun getQuestionHubDetails(questionFilter: QuestionFilter): List<QuestionHubDetail> {
        val questionInformation = questionRepository.getQuestionInformation(questionFilter)
        val creators = creatorRepository.findByIdIn(questionInformation.map { it.creatorId })
        val creatorUsers = userRepository.findByUidIn(creators.map { it.userId }).associateBy { it.uid }
        val ownedQuestionIds =
            userQuestionRepository.findByQuestionIdInAndUserId(questionInformation.map { it.id }, questionFilter.userId)
                .associateBy { it.questionId }

        return questionInformation.map {
            QuestionHubDetail(
                it,
                creatorUsers.getValue(it.creatorId).userInformation.name,
                ownedQuestionIds.contains(it.id)
            )
        }
    }

    fun getQuestionHubDetail(questionId: Long, userId: Long): QuestionHubDetail {
        val questionInformation = questionRepository.getQuestionInformation(questionId, userId)
        val creator = creatorRepository.findById(questionInformation.creatorId)
        val creatorUsers = userRepository.getUser(creator.userId)
        val isOwned = userQuestionRepository.isOwned(userId, questionId)

        return QuestionHubDetail(
            questionInformation,
            creatorUsers.userInformation.name,
            isOwned
        )
    }

    fun count(questionFilter: QuestionFilter): Int {
        return questionRepository.countByQuestionFilter(questionFilter)
    }
}