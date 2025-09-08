package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.dto.CartItemDetail
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class CartItemDetailReader(
    private val cartItemRepository: CartItemRepository,
    private val creatorQueryAPI: CreatorQueryAPI,
    private val userQueryAPI: UserQueryAPI,
    private val questionQueryAPI: QuestionQueryAPI,
) {
    fun getCartItemDetails(userId: Long): List<CartItemDetail> {
        val cartItems = cartItemRepository.findByUserId(userId)
        val questions = questionQueryAPI.getQuestionInformation(cartItems.map { it.questionId })
        val questionMap = questions.associateBy { it.id }
        
        val creators = creatorQueryAPI.getCreators(questions.map { it.creatorId })
        val creatorMap = creators.associateBy { it.creatorId }
        
        val creatorUserMap = userQueryAPI.getUsers(creators.map { it.userId }).associateBy { it.userId }
        
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
                    creatorUser.name,
                    question.subject,
                    question.price,
                )
            )
        }
        
        return cartItemDetails
    }
}