package com.eager.questioncloud.payment.document

import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import kotlinx.serialization.Serializable

@Serializable
data class QuestionPaymentHistoryOrderDocument(
    val orderItemId: Long,
    val questionId: Long,
    val originalPrice: Int,
    val realPrice: Int,
    val promotionName: String? = null,
    val promotionDiscountAmount: Int,
    val title: String,
    val thumbnail: String,
    val creatorName: String,
    val subject: String,
    val mainCategory: String,
    val subCategory: String,
) {
    companion object {
        fun from(questionPaymentHistoryOrders: List<QuestionPaymentHistoryOrder>): List<QuestionPaymentHistoryOrderDocument> {
            return questionPaymentHistoryOrders.map {
                QuestionPaymentHistoryOrderDocument(
                    orderItemId = it.orderItemId,
                    questionId = it.questionId,
                    originalPrice = it.originalPrice,
                    realPrice = it.realPrice,
                    promotionName = it.promotionName,
                    promotionDiscountAmount = it.promotionDiscountAmount,
                    title = it.title,
                    thumbnail = it.thumbnail,
                    creatorName = it.creatorName,
                    subject = it.subject,
                    mainCategory = it.mainCategory,
                    subCategory = it.subCategory,
                )
            }
        }
    }
    
    fun toModel(): QuestionPaymentHistoryOrder {
        return QuestionPaymentHistoryOrder(
            orderItemId = this.orderItemId,
            questionId = this.questionId,
            originalPrice = this.originalPrice,
            realPrice = this.realPrice,
            promotionName = this.promotionName,
            promotionDiscountAmount = this.promotionDiscountAmount,
            title = this.title,
            thumbnail = this.thumbnail,
            creatorName = this.creatorName,
            subject = this.subject,
            mainCategory = this.mainCategory,
            subCategory = this.subCategory,
        )
    }
}