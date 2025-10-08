package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.dto.QuestionPromotionData
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component

@Component
class PromotionApplier(
    private val questionQueryAPI: QuestionQueryAPI
) {
    fun apply(questionOrder: QuestionOrder) {
        val promotions = questionQueryAPI.getQuestionPromotions(questionOrder.questionIds).promotionInformation
        promotions.forEach { promotionQueryItem ->
            val orderItem = questionOrder.getOrderItem(promotionQueryItem.questionId)
            val promotionData = QuestionPromotionData(
                promotionQueryItem.id,
                promotionQueryItem.questionId,
                promotionQueryItem.title,
                promotionQueryItem.salePrice
            )
            orderItem.applyPromotion(promotionData)
        }
    }
}