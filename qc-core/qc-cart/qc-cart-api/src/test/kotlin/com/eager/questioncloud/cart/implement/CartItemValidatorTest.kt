package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
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
class CartItemValidatorTest(
    private val cartItemValidator: CartItemValidator,
    private val cartItemRepository: CartItemRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("장바구니에 추가 가능한 문제가 있을 때") {
            val userId = 100L
            val questionId = 200L
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns false
            
            When("장바구니에 추가하기 위해 검증하면") {
                Then("예외가 발생하지 않는다") {
                    shouldNotThrowAny {
                        cartItemValidator.validate(userId, questionId)
                    }
                }
            }
        }
        
        Given("이용할 수 없는 문제가 있을 때") {
            val userId = 101L
            val questionId = 201L
            
            every { questionQueryAPI.isAvailable(questionId) } returns false
            
            When("장바구니에 추가하기 위해 검증하면") {
                Then("CoreException(Error.UNAVAILABLE_QUESTION)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        cartItemValidator.validate(userId, questionId)
                    }
                    exception.error shouldBe Error.UNAVAILABLE_QUESTION
                }
            }
        }
        
        Given("이미 장바구니에 담긴 문제가 있을 때") {
            val userId = 102L
            val questionId = 202L
            
            cartItemRepository.save(CartItem.create(userId, questionId))
            every { questionQueryAPI.isAvailable(questionId) } returns true
            
            When("장바구니에 추가하기 위해 검증하면") {
                Then("CoreException(Error.ALREADY_IN_CART)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        cartItemValidator.validate(userId, questionId)
                    }
                    exception.error shouldBe Error.ALREADY_IN_CART
                }
            }
        }
        
        Given("이미 소유한 문제가 있을 때") {
            val userId = 103L
            val questionId = 203L
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns true
            
            When("장바구니에 추가하기 위해 검증하면") {
                Then("CoreException(Error.ALREADY_OWN_QUESTION)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        cartItemValidator.validate(userId, questionId)
                    }
                    exception.error shouldBe Error.ALREADY_OWN_QUESTION
                }
            }
        }
    }
}