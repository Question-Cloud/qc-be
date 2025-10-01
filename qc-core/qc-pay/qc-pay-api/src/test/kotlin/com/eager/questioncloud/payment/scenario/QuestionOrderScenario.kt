package com.eager.questioncloud.payment.scenario

import com.eager.questioncloud.payment.domain.QuestionOrder

object QuestionOrderScenario {
    fun create(orderCount: Int): QuestionOrder {
        return QuestionOrder.createOrder(QuestionOrderItemScenario.orderItems(orderCount))
    }
}