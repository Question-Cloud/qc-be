package com.eager.questioncloud.payment.scenario

import com.eager.questioncloud.payment.domain.QuestionOrderItem
import com.eager.questioncloud.payment.dto.QuestionInfo
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.KotlinTypeDefaultArbitraryBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

object QuestionOrderItemScenario {
    fun orderItems(count: Int = 3): List<QuestionOrderItem> {
        return (1..count).map {
            val questionInfo = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInfo>()
                .set(QuestionInfo::questionId, it)
                .set(QuestionInfo::creatorId, it)
                .set(QuestionInfo::price, 10000)
                .sample()
            QuestionOrderItem(questionInfo = questionInfo)
        }
    }
}

fun QuestionOrderItem.custom(
    block: KotlinTypeDefaultArbitraryBuilder<QuestionOrderItem>.() -> KotlinTypeDefaultArbitraryBuilder<QuestionOrderItem>
): QuestionOrderItem {
    return Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionOrderItem>(this)
        .block()
        .sample()
}
