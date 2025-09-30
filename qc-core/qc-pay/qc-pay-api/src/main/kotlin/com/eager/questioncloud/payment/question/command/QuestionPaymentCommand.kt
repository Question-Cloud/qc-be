package com.eager.questioncloud.payment.question.command

data class QuestionPaymentCommand(
    val userId: Long,
    val orders: List<QuestionOrderCommand>,
    val userCouponId: Long?,
) {
    val questionIds: List<Long> = orders.map { it.questionId }
}

data class QuestionOrderCommand(
    val questionId: Long,
    val userCouponIds: List<Long>,
)