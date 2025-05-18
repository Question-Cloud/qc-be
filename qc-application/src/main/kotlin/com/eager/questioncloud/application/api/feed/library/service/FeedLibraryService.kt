package com.eager.questioncloud.application.api.feed.library.service

import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionDetail
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import org.springframework.stereotype.Service

@Service
class FeedLibraryService(
    private val userQuestionRepository: UserQuestionRepository
) {
    fun getUserQuestions(questionFilter: QuestionFilter): List<UserQuestionDetail> {
        return userQuestionRepository.getUserQuestions(questionFilter)
    }

    fun countUserQuestions(questionFilter: QuestionFilter): Int {
        return userQuestionRepository.countUserQuestions(questionFilter)
    }
}
