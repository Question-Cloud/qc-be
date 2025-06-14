package com.eager.questioncloud.userquestion.infrastructure.repository

import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.userquestion.domain.UserQuestion
import com.eager.questioncloud.userquestion.dto.UserQuestionContent

interface UserQuestionRepository {
    fun saveAll(userQuestionLibraries: List<UserQuestion>): List<UserQuestion>

    fun isOwned(userId: Long, questionIds: List<Long>): Boolean

    fun isOwned(userId: Long, questionId: Long): Boolean

    fun getUserQuestions(questionFilter: QuestionFilter): List<UserQuestionContent>

    fun countUserQuestions(questionFilter: QuestionFilter): Int

    fun deleteAllInBatch()

    fun findByQuestionIdInAndUserId(questionIds: List<Long>, userId: Long): List<UserQuestion>
}
