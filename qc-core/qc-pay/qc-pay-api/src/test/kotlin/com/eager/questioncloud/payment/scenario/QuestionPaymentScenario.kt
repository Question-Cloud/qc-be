package com.eager.questioncloud.payment.scenario

import com.eager.questioncloud.payment.domain.*
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

class QuestionPaymentScenario(
    val questionPayment: QuestionPayment,
    val questionInformationQueryResult: List<QuestionInformationQueryResult>,
) {
    companion object {
        fun create(userId: Long, orderCount: Int, userCoupon: UserCoupon? = null, coupon: Coupon? = null): QuestionPaymentScenario {
            val questionInformationQueryResult = (1..orderCount).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, it)
                    .set(QuestionInformationQueryResult::price, (1000..3000).random())
                    .sample()
            }
            
            val questionOrder = QuestionOrder.createOrder(
                questionInformationQueryResult
                    .map { QuestionOrderItem(questionId = it.id, price = it.price) }
            )
            
            val questionPaymentCoupon = if (userCoupon != null && coupon != null) {
                QuestionPaymentCoupon.create(userCoupon.id, coupon)
            } else null
            
            return QuestionPaymentScenario(
                QuestionPayment.create(userId, questionPaymentCoupon, questionOrder),
                questionInformationQueryResult
            )
        }
    }
}