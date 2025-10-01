package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.Promotion
import com.eager.questioncloud.payment.repository.PromotionRepository
import com.eager.questioncloud.payment.scenario.PromotionScenario
import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class PromotionApplierTest(
    private val promotionApplier: PromotionApplier,
    private val promotionRepository: PromotionRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("프로모션 중인 상품이 포함된 주문이 있을 때") {
            val questionOrder = QuestionOrderScenario.create(10)
            val promotions = mutableListOf<Promotion>()
            val promotionCount = questionOrder.items.size / 2
            questionOrder.items.shuffled().take(promotionCount)
                .forEach { item ->
                    val savedPromotion = promotionRepository.save(
                        PromotionScenario.create(item.questionInfo.questionId, (item.originalPrice * (10..60).random() / 100))
                    )
                    promotions.add(savedPromotion)
                }
            
            When("프로모션을 적용하면") {
                promotionApplier.apply(questionOrder)
                Then("주문 가격이 프로모션 가격으로 적용된다.") {
                    val promotionMapByQuestionId = promotions.associateBy { it.questionId }
                    questionOrder.items.forEach {
                        if (promotionMapByQuestionId.containsKey(it.questionInfo.questionId)) {
                            it.realPrice shouldBe promotionMapByQuestionId.getValue(it.questionInfo.questionId).salePrice
                        } else {
                            it.realPrice shouldBe it.originalPrice
                        }
                    }
                }
            }
        }
    }
}
