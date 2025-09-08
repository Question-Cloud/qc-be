package com.eager.questioncloud.question.product.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.repository.QuestionRepository
import com.eager.questioncloud.question.repository.UserQuestionRepository
import com.eager.questioncloud.question.product.dto.StoreProductDetail
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class StoreProductDetailReader(
    private val questionRepository: QuestionRepository,
    private val creatorQueryAPI: CreatorQueryAPI,
    private val userQueryAPI: UserQueryAPI,
    private val userQuestionRepository: UserQuestionRepository
) {
    fun getStoreProductDetails(
        userId: Long,
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): List<StoreProductDetail> {
        val questionInformation = questionRepository.getQuestionInformation(questionFilter, pagingInformation)
        val creators = creatorQueryAPI.getCreators(questionInformation.map { it.creatorId })
        val creatorMap = creators.associateBy { it.creatorId }
        val creatorUsers = userQueryAPI.getUsers(creators.map { it.userId }).associateBy { it.userId }
        val ownedQuestionIds =
            userQuestionRepository.findByQuestionIdInAndUserId(questionInformation.map { it.id }, userId)
                .associateBy { it.questionId }
        
        return questionInformation.map {
            val creator = creatorMap.getValue(it.creatorId)
            val creatorUser = creatorUsers.getValue(creator.userId)
            StoreProductDetail(
                it,
                creatorUser.name,
                ownedQuestionIds.contains(it.id)
            )
        }
    }
    
    fun getStoreProductDetail(questionId: Long, userId: Long): StoreProductDetail {
        val questionInformation = questionRepository.getQuestionInformation(questionId)
        val creator = creatorQueryAPI.getCreator(questionInformation.creatorId)
        val creatorUsers = userQueryAPI.getUser(creator.userId)
        val isOwned = userQuestionRepository.isOwned(userId, questionId)
        
        return StoreProductDetail(
            questionInformation,
            creatorUsers.name,
            isOwned
        )
    }
    
    fun count(questionFilter: QuestionFilter): Int {
        return questionRepository.countByQuestionFilter(questionFilter)
    }
}