package com.eager.questioncloud.common.event

import io.hypersistence.tsid.TSID

class QuestionPaymentEvent(
    val paymentId: Long,
    val orderId: String,
    val buyerUserId: Long,
    val questionIds: List<Long>,
    val amount: Int,
) : Event(TSID.Factory.getTsid().toString(), EventType.QuestionPaymentEvent)

