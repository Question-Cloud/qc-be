package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionOrderRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder.Companion.createOrder
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Component

@Component
class QuestionOrderGenerator(
    private val userQuestionRepository: UserQuestionRepository,
    private val questionRepository: QuestionRepository,
    private val questionOrderRepository: QuestionOrderRepository,
) {
    fun generateQuestionOrder(userId: Long, questionIds: List<Long>): QuestionOrder {
        if (checkAlreadyOwned(userId, questionIds)) {
            throw CoreException(Error.ALREADY_OWN_QUESTION)
        }

        val questions = getQuestions(questionIds)
        val questionOrder = createOrder(questions)

        questionOrderRepository.save(questionOrder)
        return questionOrder
    }

    private fun checkAlreadyOwned(userId: Long, questionIds: List<Long>): Boolean {
        return userQuestionRepository.isOwned(userId, questionIds)
    }

    private fun getQuestions(questionIds: List<Long>): List<Question> {
        val questions = questionRepository.getQuestionsByQuestionIds(questionIds)
        if (questions.size != questionIds.size) {
            throw CoreException(Error.UNAVAILABLE_QUESTION)
        }
        return questions
    }
}
