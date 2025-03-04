package com.eager.questioncloud.application.business.review.implement

import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component

@Component
class HubReviewRegister(
    private val questionReviewRepository: QuestionReviewRepository,
    private val questionRepository: QuestionRepository,
    private val userQuestionRepository: UserQuestionRepository,
) {
    fun register(questionReview: QuestionReview) {
        try {
            if (isUnAvailableQuestion(questionReview.questionId)) {
                throw CoreException(Error.UNAVAILABLE_QUESTION)
            }

            if (isNotOwnedQuestion(questionReview.reviewerId, questionReview.questionId)) {
                throw CoreException(Error.NOT_OWNED_QUESTION)
            }

            if (isAlreadyWrittenReview(questionReview.reviewerId, questionReview.questionId)) {
                throw CoreException(Error.ALREADY_REGISTER_REVIEW)
            }
            questionReviewRepository.save(questionReview)
        } catch (e: DataIntegrityViolationException) {
            throw CoreException(Error.ALREADY_REGISTER_REVIEW)
        }
    }

    private fun isUnAvailableQuestion(questionId: Long): Boolean {
        return !questionRepository.isAvailable(questionId)
    }

    private fun isNotOwnedQuestion(userId: Long, questionId: Long): Boolean {
        return !userQuestionRepository.isOwned(userId, questionId)
    }

    private fun isAlreadyWrittenReview(userId: Long, questionId: Long): Boolean {
        return questionReviewRepository.isWritten(userId, questionId)
    }
}
