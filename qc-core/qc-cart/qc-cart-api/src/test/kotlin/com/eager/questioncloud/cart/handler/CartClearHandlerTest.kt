package com.eager.questioncloud.cart.handler

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.common.event.CouponUsageInformation
import com.eager.questioncloud.common.event.QuestionPaymentEvent
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CartClearHandlerTest(
    private val cartClearHandler: CartClearHandler,
    private val cartItemRepository: CartItemRepository,
    private val dbCleaner: DBCleaner
) : BehaviorSpec() {
    init {
        
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("장바구니에 문제들이 담겨 있을 때") {
            val userId = 100L
            
            val boughtQuestionId1 = 1L
            val boughtQuestionId2 = 2L
            val nonBoughtquestionId3 = 3L
            
            cartItemRepository.save(CartItem.create(userId, boughtQuestionId1))
            cartItemRepository.save(CartItem.create(userId, boughtQuestionId2))
            cartItemRepository.save(CartItem.create(userId, nonBoughtquestionId3))
            
            val questionPaymentEvent = QuestionPaymentEvent(
                orderId = UUID.randomUUID().toString(),
                buyerUserId = userId,
                questionIds = listOf(boughtQuestionId1, boughtQuestionId2),
                amount = 10000,
                couponUsageInformation = CouponUsageInformation("할인 미적용", 0)
            )
            
            When("QuestionPaymentEvent를 처리하면") {
                cartClearHandler.clearCart(questionPaymentEvent)
                
                Then("구매한 문제는 장바구니에서 제거 된다.") {
                    val cartItems = cartItemRepository.findByUserId(userId)
                    cartItems shouldHaveSize 1
                    cartItems[0].questionId shouldBe nonBoughtquestionId3
                }
            }
        }
    }
}
