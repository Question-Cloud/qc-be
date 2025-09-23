package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
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
        
        Given("장바구니에 문제를 추가하는 상황에서") {
            When("이용 가능하고 소유하지 않은 문제라면") {
                val userId = 100L
                val questionId = 200L
                
                every { questionQueryAPI.isAvailable(questionId) } returns true
                every { questionQueryAPI.isOwned(userId, questionId) } returns false
                
                cartItemAppender.append(userId, questionId)
                
                Then("장바구니에 추가된다") {
                    val cartItems = cartItemRepository.findByUserId(userId)
                    cartItems shouldHaveSize 1
                    
                    val cartItem = cartItems[0]
                    cartItem.userId shouldBe userId
                    cartItem.questionId shouldBe questionId
                }
            }
            
            When("이용할 수 없는 문제라면") {
                val userId = 101L
                val questionId = 201L
                
                every { questionQueryAPI.isAvailable(questionId) } returns false
                
                Then("CoreException(Error.UNAVAILABLE_QUESTION)이 발생한다") {
                    shouldThrow<CoreException> {
                        cartItemAppender.append(userId, questionId)
                    }
                    cartItemRepository.findByUserId(userId).shouldBeEmpty()
                }
            }
            
            When("이미 장바구니에 있는 문제라면") {
                val userId = 102L
                val questionId = 202L
                
                cartItemRepository.save(CartItem.create(userId, questionId))
                
                every { questionQueryAPI.isAvailable(questionId) } returns true
                
                Then("CoreException(Error.ALREADY_IN_CART)이 발생한다") {
                    shouldThrow<CoreException> {
                        cartItemAppender.append(userId, questionId)
                    }
                    cartItemRepository.findByUserId(userId) shouldHaveSize 1
                }
            }
            
            When("이미 소유한 문제라면") {
                val userId = 103L
                val questionId = 203L
                
                every { questionQueryAPI.isAvailable(questionId) } returns true
                every { questionQueryAPI.isOwned(userId, questionId) } returns true
                
                Then("CoreException(Error.ALREADY_OWN_QUESTION)이 발생한다") {
                    shouldThrow<CoreException> {
                        cartItemAppender.append(userId, questionId)
                    }
                    cartItemRepository.findByUserId(userId).shouldBeEmpty()
                }
            }
        }
    }
}
