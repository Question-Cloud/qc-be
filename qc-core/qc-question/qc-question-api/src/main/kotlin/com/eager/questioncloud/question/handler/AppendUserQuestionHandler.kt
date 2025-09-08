package com.eager.questioncloud.question.handler

import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.repository.UserQuestionRepository
import org.springframework.stereotype.Component

@Component
class AppendUserQuestionHandler(
    private val userQuestionRepository: UserQuestionRepository,
) {
    fun appendUserQuestion(event: QuestionPaymentEvent) {
        userQuestionRepository.saveAll(UserQuestion.create(event.buyerUserId, event.questionIds))
    }
}
