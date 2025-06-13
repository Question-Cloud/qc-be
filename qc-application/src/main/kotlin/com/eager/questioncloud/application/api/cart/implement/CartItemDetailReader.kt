package com.eager.questioncloud.application.api.cart.implement

import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail
import com.eager.questioncloud.core.domain.cart.infrastructure.repository.CartItemRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import org.springframework.stereotype.Component

@Component
class CartItemDetailReader(
    private val cartItemRepository: CartItemRepository,
    private val questionRepository: QuestionRepository
) {
    fun getCartItemDetails(userId: Long): List<CartItemDetail> {
        val cartItems = cartItemRepository.findByUserId(userId)
        val questions = questionRepository.findByQuestionIdIn(cartItems.map { it.questionId }).associateBy { it.id }

        val cartItemDetails = mutableListOf<CartItemDetail>()

        for (cartItem in cartItems) {
            val question = questions.getValue(cartItem.questionId)
            cartItemDetails.add(
                CartItemDetail(
                    cartItem.id,
                    question.id,
                    question.title,
                    question.thumbnail,
                    question.creatorName,
                    question.subject,
                    question.price,
                )
            )
        }

        return cartItemDetails
    }
}