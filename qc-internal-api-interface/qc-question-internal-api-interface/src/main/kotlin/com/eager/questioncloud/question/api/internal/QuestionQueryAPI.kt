package com.eager.questioncloud.question.api.internal

import com.eager.questioncloud.common.pagination.PagingInformation

interface QuestionQueryAPI {
    fun getQuestionInformation(questionId: Long): QuestionInformationQueryResult
    
    fun getQuestionInformation(questionIds: List<Long>): List<QuestionInformationQueryResult>
    
    fun isAvailable(questionId: Long): Boolean
    
    fun isOwned(userId: Long, questionId: Long): Boolean
    
    fun isOwned(userId: Long, questionIds: List<Long>): Boolean
    
    fun getCreatorQuestions(
        creatorId: Long,
        pagingInformation: PagingInformation = PagingInformation.max
    ): List<QuestionInformationQueryResult>
    
    fun countByCreatorId(creatorId: Long): Int
    
    fun getQuestionContent(questionId: Long, creatorId: Long): QuestionContentQueryResult
    
    fun getQuestionPromotions(questionIds: List<Long>): QuestionPromotionQueryResult
}