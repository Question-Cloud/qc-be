package com.eager.questioncloud.application.business.cart.implement

import com.eager.questioncloud.core.domain.cart.infrastructure.repository.CartItemRepository
import com.eager.questioncloud.core.domain.cart.model.CartItem.Companion.create
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Component

@Component
class CartItemAppender(
    private val questionRepository: QuestionRepository,
    private val cartItemRepository: CartItemRepository,
    private val userQuestionRepository: UserQuestionRepository,
) {
    fun append(userId: Long, questionId: Long) {
        if (isUnAvailableQuestion(questionId)) {
            throw CoreException(Error.UNAVAILABLE_QUESTION)
        }

        if (isAlreadyInCart(userId, questionId)) {
            throw CoreException(Error.ALREADY_IN_CART)
        }

        if (isAlreadyOwned(userId, questionId)) {
            throw CoreException(Error.ALREADY_OWN_QUESTION)
        }

        cartItemRepository.save(create(userId, questionId))
    }

    private fun isUnAvailableQuestion(questionId: Long): Boolean {
        return !questionRepository.isAvailable(questionId)
    }

    private fun isAlreadyInCart(userId: Long, questionId: Long): Boolean {
        return cartItemRepository.isExistsInCart(userId, questionId)
    }

    private fun isAlreadyOwned(userId: Long, questionId: Long): Boolean {
        return userQuestionRepository.isOwned(userId, questionId)
    }
}
