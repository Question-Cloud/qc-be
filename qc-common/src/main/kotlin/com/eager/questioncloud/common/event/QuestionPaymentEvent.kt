package com.eager.questioncloud.common.event

import io.hypersistence.tsid.TSID

class QuestionPaymentEvent(
    val orderId: String,
    val buyerUserId: Long,
    val questionIds: List<Long>,
    val amount: Int,
    val questionPaymentCoupon: QuestionPaymentEventCouponData?,
) : Event(TSID.Factory.getTsid().toString(), EventType.QuestionPaymentEvent)

class QuestionPaymentEventCouponData(
    val userCouponId: Long,
    val couponId: Long,
    val title: String,
    val couponType: String,
    val value: Int,
)