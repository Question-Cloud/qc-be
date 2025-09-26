package com.eager.questioncloud.payment.scenario

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionOrderItem
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class QuestionPaymentScenario(
    val order: QuestionOrder,
    val questionInformationQueryResult: List<QuestionInformationQueryResult>,
) {
    companion object {
        fun create(orderCount: Int): QuestionPaymentScenario {
            val questionInformationQueryResult = (1..orderCount).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, it)
                    .set(QuestionInformationQueryResult::price, (1000..3000).random())
                    .sample()
            }
            
            val order = QuestionOrder.createOrder(
                questionInformationQueryResult
                    .map { QuestionOrderItem(questionId = it.id, price = it.price) }
            )
            
            return QuestionPaymentScenario(order, questionInformationQueryResult)
        }
    }
}