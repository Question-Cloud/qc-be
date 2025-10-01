package com.eager.questioncloud.payment.question.command

data class QuestionPaymentCommand(
    val userId: Long,
    val orders: List<QuestionOrderCommand>,
    val paymentUserCouponId: Long? = null,
) {
    val questionIds: List<Long> = orders.map { it.questionId }
    val allUserCouponIds: List<Long> = orders.flatMap { it.userCouponIds }
}

data class QuestionOrderCommand(
    val questionId: Long,
    val orderUserCouponId: Long? = null,
    val duplicableOrderUserCouponId: Long? = null,
) {
    val userCouponIds: List<Long>
        get() = listOfNotNull(orderUserCouponId, duplicableOrderUserCouponId)
}