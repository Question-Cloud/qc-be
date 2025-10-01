package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.repository.PromotionRepository
import org.springframework.stereotype.Component

@Component
class PromotionApplier(
    private val promotionRepository: PromotionRepository
) {
    fun apply(questionOrder: QuestionOrder) {
        val promotions = promotionRepository.findByQuestionIdIn(questionOrder.questionIds)
        promotions.forEach { promotion ->
            val orderItem = questionOrder.getOrderItem(promotion.questionId)
            orderItem.applyPromotion(promotion)
        }
    }
}