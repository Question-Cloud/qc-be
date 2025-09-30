package com.eager.questioncloud.payment.question.command

data class QuestionPaymentCommand(
    val userId: Long,
    val orders: List<QuestionOrderCommand>,
    val paymentUserCouponId: Long?,
) {
    val questionIds: List<Long> = orders.map { it.questionId }
}

data class QuestionOrderCommand(
    val questionId: Long,
    val orderUserCouponIds: List<Long>,
)