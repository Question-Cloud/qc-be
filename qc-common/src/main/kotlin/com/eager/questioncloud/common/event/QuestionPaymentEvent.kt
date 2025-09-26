package com.eager.questioncloud.common.event

import io.hypersistence.tsid.TSID

class QuestionPaymentEvent(
    val orderId: String,
    val buyerUserId: Long,
    val questionIds: List<Long>,
    val amount: Int,
    val couponUsageInformation: CouponUsageInformation,
) : Event(TSID.Factory.getTsid().toString(), EventType.QuestionPaymentEvent)

class CouponUsageInformation(
    val title: String,
    val value: Int,
)