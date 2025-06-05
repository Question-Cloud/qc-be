package com.eager.questioncloud.application.utils.fixture.helper

import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion

class UserQuestionFixtureHelper {
    companion object {
        fun createUserQuestion(
            userId: Long,
            questionId: Long,
            userQuestionRepository: UserQuestionRepository
        ): UserQuestion {
            val userQuestion = UserQuestion.create(userId = userId, questionId = questionId)
            return userQuestionRepository.saveAll(listOf(userQuestion)).first()
        }

        fun createUserQuestions(
            userId: Long,
            questionIds: List<Long>,
            userQuestionRepository: UserQuestionRepository
        ): List<UserQuestion> {
            val userQuestions = UserQuestion.create(userId = userId, questionIds = questionIds)
            return userQuestionRepository.saveAll(userQuestions)
        }
    }
}
