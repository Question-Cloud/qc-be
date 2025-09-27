package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.Promotion
import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionOrderItem
import com.eager.questioncloud.payment.repository.PromotionRepository
import com.eager.questioncloud.payment.repository.QuestionOrderRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component

@Component
class QuestionOrderGenerator(
    private val questionQueryAPI: QuestionQueryAPI,
    private val questionOrderRepository: QuestionOrderRepository,
    private val promotionRepository: PromotionRepository,
) {
    fun generateQuestionOrder(userId: Long, questionIds: List<Long>): QuestionOrder {
        if (checkAlreadyOwned(userId, questionIds)) {
            throw CoreException(Error.ALREADY_OWN_QUESTION)
        }
        
        val questions = getQuestions(questionIds)
        val promotions = promotionRepository.findByQuestionIdIn(questionIds).associateBy { it.questionId }
        val orderItems = createOrderItems(questions, promotions)
        val orders = QuestionOrder.createOrder(orderItems)
        
        questionOrderRepository.save(orders)
        return orders
    }
    
    private fun checkAlreadyOwned(userId: Long, questionIds: List<Long>): Boolean {
        return questionQueryAPI.isOwned(userId, questionIds)
    }
    
    private fun createOrderItems(
        questions: List<QuestionInformationQueryResult>,
        promotions: Map<Long, Promotion>
    ): List<QuestionOrderItem> {
        return questions.map {
            val promotion = promotions[it.id]
            if (promotion == null) QuestionOrderItem(questionId = it.id, originalPrice = it.price)
            else QuestionOrderItem(questionId = it.id, originalPrice = it.price, promotion = promotion.toDiscountPolicy())
        }
    }
    
    private fun getQuestions(questionIds: List<Long>): List<QuestionInformationQueryResult> {
        val questions = questionQueryAPI.getQuestionInformation(questionIds)
        
        if (questions.size != questionIds.size) {
            throw CoreException(Error.UNAVAILABLE_QUESTION)
        }
        
        return questions
    }
}
