package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import com.eager.questioncloud.question.api.internal.QuestionPromotionQueryItem
import com.eager.questioncloud.question.api.internal.QuestionPromotionQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class PromotionApplierTest(
    private val promotionApplier: PromotionApplier,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("프로모션 중인 상품이 포함된 주문이 있을 때") {
            val questionOrder = QuestionOrderScenario.create(10)
            val promotionPromotionQueryItems = mutableListOf<QuestionPromotionQueryItem>()
            val promotionCount = questionOrder.items.size / 2
            questionOrder.items.shuffled().take(promotionCount)
                .forEachIndexed { idx, item ->
                    promotionPromotionQueryItems.add(
                        QuestionPromotionQueryItem(
                            idx.toLong(),
                            item.questionInfo.questionId,
                            "promotion ${item.questionInfo.questionId}",
                            (item.originalPrice * (10..60).random() / 100)
                        )
                    )
                }
            
            every { questionQueryAPI.getQuestionPromotions(any()) } returns QuestionPromotionQueryResult(promotionPromotionQueryItems)
            
            
            When("프로모션을 적용하면") {
                promotionApplier.apply(questionOrder)
                Then("주문 가격이 프로모션 가격으로 적용된다.") {
                    val promotionMapByQuestionId = promotionPromotionQueryItems.associateBy { it.questionId }
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
