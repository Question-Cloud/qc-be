package com.eager.questioncloud.payment.domain

import io.hypersistence.tsid.TSID
import java.util.stream.Collectors

class QuestionOrder(
    var orderId: String,
    var items: List<QuestionOrderItem>
) {
    fun calcAmount(): Int {
        return items
            .stream()
            .mapToInt { obj: QuestionOrderItem -> obj.price }
            .sum()
    }

    val questionIds: List<Long>
        get() = items
            .stream()
            .map { obj: QuestionOrderItem -> obj.questionId }
            .collect(Collectors.toList())

    companion object {
        fun createOrder(orderItems: List<QuestionOrderItem>): QuestionOrder {
            val orderId = TSID.Factory.getTsid().toString()
            return QuestionOrder(orderId, orderItems)
        }
    }
}
