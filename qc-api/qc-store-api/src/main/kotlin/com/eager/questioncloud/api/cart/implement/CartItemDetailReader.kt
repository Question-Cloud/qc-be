package com.eager.questioncloud.api.cart.implement

import com.eager.questioncloud.cart.dto.CartItemDetail
import com.eager.questioncloud.cart.infrastructure.repository.CartItemRepository
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class CartItemDetailReader(
    private val cartItemRepository: CartItemRepository,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
    private val creatorRepository: CreatorRepository
) {
    fun getCartItemDetails(userId: Long): List<CartItemDetail> {
        val cartItems = cartItemRepository.findByUserId(userId)
        val questions = questionRepository.findByQuestionIdIn(cartItems.map { it.questionId })
        val questionMap = questions.associateBy { it.id }

        val creators = creatorRepository.findByIdIn(questions.map { it.creatorId })
        val creatorMap = creators.associateBy { it.id }

        val creatorUserMap = userRepository.findByUidIn(creators.map { it.userId }).associateBy { it.uid }

        val cartItemDetails = mutableListOf<CartItemDetail>()

        for (cartItem in cartItems) {
            val question = questionMap.getValue(cartItem.questionId)
            val creator = creatorMap.getValue(question.creatorId)
            val creatorUser = creatorUserMap.getValue(creator.userId)

            cartItemDetails.add(
                CartItemDetail(
                    cartItem.id,
                    question.id,
                    question.title,
                    question.thumbnail,
                    creatorUser.userInformation.name,
                    question.subject,
                    question.price,
                )
            )
        }

        return cartItemDetails
    }
}