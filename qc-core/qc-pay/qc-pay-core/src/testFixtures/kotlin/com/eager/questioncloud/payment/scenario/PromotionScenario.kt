package com.eager.questioncloud.payment.scenario

import com.eager.questioncloud.payment.domain.Promotion
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

object PromotionScenario {
    fun create(questionId: Long, salePrice: Int = 1000): Promotion {
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<Promotion>()
            .set(Promotion::questionId, questionId)
            .set(Promotion::salePrice, salePrice)
            .set(Promotion::isActive, true)
            .sample()
    }
}