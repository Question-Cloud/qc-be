package com.eager.questioncloud.core.domain.userquestion.infrastructure.repository

import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionDetail
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion

interface UserQuestionRepository {
    fun saveAll(userQuestionLibraries: List<UserQuestion>): List<UserQuestion>

    fun isOwned(userId: Long, questionIds: List<Long>): Boolean

    fun isOwned(userId: Long, questionId: Long): Boolean

    fun getUserQuestions(questionFilter: QuestionFilter): List<UserQuestionDetail>

    fun countUserQuestions(questionFilter: QuestionFilter): Int

    fun deleteAllInBatch()
}
