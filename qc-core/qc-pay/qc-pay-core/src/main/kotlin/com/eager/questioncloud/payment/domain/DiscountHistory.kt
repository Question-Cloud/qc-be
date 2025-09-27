package com.eager.questioncloud.payment.domain

class DiscountHistory(
    val id: Long = 0,
    val orderId: String,
    val discountType: DiscountType,
    val appliedAmount: Int,
    val name: String,
    val sourceId: Long,
) {
    companion object {
        fun create(questionPayment: QuestionPayment): List<DiscountHistory> {
            val discountList = mutableListOf<DiscountHistory>()
            questionPayment.appliedDiscountList.forEach {
                discountList.add(
                    DiscountHistory(
                        orderId = questionPayment.order.orderId,
                        discountType = it.getDiscountType(),
                        appliedAmount = it.getAppliedDiscountAmount(),
                        name = it.getName(),
                        sourceId = it.getSourceId()
                    )
                )
            }
            return discountList
        }
    }
}