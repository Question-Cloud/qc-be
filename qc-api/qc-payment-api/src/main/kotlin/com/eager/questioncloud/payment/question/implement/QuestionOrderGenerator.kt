package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.infrastructure.repository.QuestionOrderRepository
import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.userquestion.infrastructure.repository.UserQuestionRepository
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
        val questionOrder = QuestionOrder.createOrder(questions)

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
