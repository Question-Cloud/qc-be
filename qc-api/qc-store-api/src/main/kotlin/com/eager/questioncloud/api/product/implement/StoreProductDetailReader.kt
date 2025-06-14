package com.eager.questioncloud.api.product.implement

import com.eager.questioncloud.api.product.dto.StoreProductDetail
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.userquestion.infrastructure.repository.UserQuestionRepository
import org.springframework.stereotype.Component

@Component
class StoreProductDetailReader(
    private val questionRepository: QuestionRepository,
    private val creatorRepository: CreatorRepository,
    private val userRepository: UserRepository,
    private val userQuestionRepository: UserQuestionRepository
) {
    fun getQuestionHubDetails(questionFilter: QuestionFilter): List<StoreProductDetail> {
        val questionInformation = questionRepository.getQuestionInformation(questionFilter)
        val creators = creatorRepository.findByIdIn(questionInformation.map { it.creatorId })
        val creatorUsers = userRepository.findByUidIn(creators.map { it.userId }).associateBy { it.uid }
        val ownedQuestionIds =
            userQuestionRepository.findByQuestionIdInAndUserId(questionInformation.map { it.id }, questionFilter.userId)
                .associateBy { it.questionId }

        return questionInformation.map {
            StoreProductDetail(
                it,
                creatorUsers.getValue(it.creatorId).userInformation.name,
                ownedQuestionIds.contains(it.id)
            )
        }
    }

    fun getQuestionHubDetail(questionId: Long, userId: Long): StoreProductDetail {
        val questionInformation = questionRepository.getQuestionInformation(questionId, userId)
        val creator = creatorRepository.findById(questionInformation.creatorId)
        val creatorUsers = userRepository.getUser(creator.userId)
        val isOwned = userQuestionRepository.isOwned(userId, questionId)

        return StoreProductDetail(
            questionInformation,
            creatorUsers.userInformation.name,
            isOwned
        )
    }

    fun count(questionFilter: QuestionFilter): Int {
        return questionRepository.countByQuestionFilter(questionFilter)
    }
}