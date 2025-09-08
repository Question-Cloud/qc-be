package com.eager.questioncloud.question.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.dto.UserQuestionContent

interface UserQuestionRepository {
    fun saveAll(userQuestionLibraries: List<UserQuestion>): List<UserQuestion>
    
    fun isOwned(userId: Long, questionIds: List<Long>): Boolean
    
    fun isOwned(userId: Long, questionId: Long): Boolean
    
    fun getUserQuestions(
        userId: Long,
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): List<UserQuestionContent>
    
    fun countUserQuestions(userId: Long, questionFilter: QuestionFilter): Int
    
    fun deleteAllInBatch()
    
    fun findByQuestionIdInAndUserId(questionIds: List<Long>, userId: Long): List<UserQuestion>
}
