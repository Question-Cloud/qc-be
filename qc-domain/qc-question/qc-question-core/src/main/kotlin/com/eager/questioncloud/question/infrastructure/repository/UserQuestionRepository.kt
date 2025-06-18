package com.eager.questioncloud.question.infrastructure.repository

import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.dto.UserQuestionContent

interface UserQuestionRepository {
    fun saveAll(userQuestionLibraries: List<UserQuestion>): List<UserQuestion>

    fun isOwned(userId: Long, questionIds: List<Long>): Boolean

    fun isOwned(userId: Long, questionId: Long): Boolean

    fun getUserQuestions(questionFilter: QuestionFilter): List<UserQuestionContent>

    fun countUserQuestions(questionFilter: QuestionFilter): Int

    fun deleteAllInBatch()

    fun findByQuestionIdInAndUserId(questionIds: List<Long>, userId: Long): List<UserQuestion>
}
