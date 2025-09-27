package com.eager.questioncloud.payment.domain

class DiscountHistory(
    val id: Long = 0,
    val orderId: String,
    val discountType: DiscountType,
    val sourceId: Long,
) {
    companion object {
        fun create(questionPayment: QuestionPayment): List<DiscountHistory> {
            return questionPayment.discountPolicy.map {
                DiscountHistory(orderId = questionPayment.order.orderId, discountType = it.getDiscountType(), sourceId = it.getSourceId())
            }
        }
    }
}