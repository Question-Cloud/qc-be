package com.eager.questioncloud.review.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.review.command.RegisterReviewCommand
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component

@Component
class StoreReviewRegister(
    private val questionReviewRepository: QuestionReviewRepository,
    private val questionQueryAPI: QuestionQueryAPI
) {
    fun register(command: RegisterReviewCommand): QuestionReview {
        return runCatching {
            val questionReview = QuestionReview.create(command.questionId, command.reviewerId, command.comment, command.rate)
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
        }.onFailure {
            if (it is DataIntegrityViolationException) {
                throw CoreException(Error.ALREADY_REGISTER_REVIEW)
            }
            
            throw it
        }.getOrThrow()
    }
    
    private fun isUnAvailableQuestion(questionId: Long): Boolean {
        return !questionQueryAPI.isAvailable(questionId)
    }
    
    private fun isNotOwnedQuestion(userId: Long, questionId: Long): Boolean {
        return !questionQueryAPI.isOwned(userId, questionId)
    }
    
    private fun isAlreadyWrittenReview(userId: Long, questionId: Long): Boolean {
        return questionReviewRepository.isWritten(userId, questionId)
    }
}
