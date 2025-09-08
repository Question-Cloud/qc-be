package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component

@Component
class CartItemAppender(
    private val cartItemRepository: CartItemRepository,
    private val questionQueryAPI: QuestionQueryAPI,
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
        
        cartItemRepository.save(CartItem.create(userId, questionId))
    }
    
    private fun isUnAvailableQuestion(questionId: Long): Boolean {
        return !questionQueryAPI.isAvailable(questionId)
    }
    
    private fun isAlreadyInCart(userId: Long, questionId: Long): Boolean {
        return cartItemRepository.isExistsInCart(userId, questionId)
    }
    
    private fun isAlreadyOwned(userId: Long, questionId: Long): Boolean {
        return questionQueryAPI.isOwned(userId, questionId)
    }
}
