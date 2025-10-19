package com.eager.questioncloud.review.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.review.command.RegisterReviewCommand
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import org.springframework.stereotype.Component

@Component
class StoreReviewValidator(
    private val questionQueryAPI: QuestionQueryAPI,
    private val questionReviewRepository: QuestionReviewRepository,
) {
    fun validate(command: RegisterReviewCommand) {
        if (isUnAvailableQuestion(command.questionId)) {
            throw CoreException(Error.UNAVAILABLE_QUESTION)
        }
        
        if (isNotOwnedQuestion(command.reviewerId, command.questionId)) {
            throw CoreException(Error.NOT_OWNED_QUESTION)
        }
        
        if (isAlreadyWrittenReview(command.reviewerId, command.questionId)) {
            throw CoreException(Error.ALREADY_REGISTER_REVIEW)
        }
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