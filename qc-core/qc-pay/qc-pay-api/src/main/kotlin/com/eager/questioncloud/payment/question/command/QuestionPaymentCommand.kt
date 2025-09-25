package com.eager.questioncloud.payment.question.command

data class QuestionPaymentCommand(
    val userId: Long,
    val questionIds: List<Long>,
    val userCouponId: Long?,
)
