package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.test.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CartItemAppenderTest(
    private val cartItemAppender: CartItemAppender,
    private val cartItemRepository: CartItemRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("장바구니 문제 추가") {
            When("장바구니에 문제를 추가하면") {
                val userId = 100L
                val questionId = 200L
                
                every { questionQueryAPI.isAvailable(questionId) } returns true
                every { questionQueryAPI.isOwned(userId, questionId) } returns false
                
                cartItemAppender.append(userId, questionId)
                
                Then("장바구니에 문제가 추가된다") {
                    val cartItems = cartItemRepository.findByUserId(userId)
                    cartItems shouldHaveSize 1
                    
                    val cartItem = cartItems[0]
                    cartItem.userId shouldBe userId
                    cartItem.questionId shouldBe questionId
                }
            }
        }
    }
}
