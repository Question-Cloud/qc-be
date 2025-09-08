package com.eager.questioncloud.common.event

class QuestionPaymentEventPayload(
    val orderId: String,
    val buyerUserId: Long,
    val questionIds: List<Long>,
    val amount: Int,
    val questionPaymentCoupon: QuestionPaymentEventCouponData?,
)

class QuestionPaymentEventCouponData(
    val userCouponId: Long,
    val couponId: Long,
    val title: String,
    val couponType: String,
    val value: Int,
)