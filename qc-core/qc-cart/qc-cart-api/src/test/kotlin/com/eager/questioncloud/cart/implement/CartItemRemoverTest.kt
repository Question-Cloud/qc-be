package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CartItemRemoverTest(
    private val cartItemRemover: CartItemRemover,
    private val cartItemRepository: CartItemRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("장바구니에 여러 문제가 담겨있을 때") {
            val userId = 100L
            val cartItem1 = cartItemRepository.save(CartItem.create(userId, 200L))
            val cartItem2 = cartItemRepository.save(CartItem.create(userId, 201L))
            val cartItem3 = cartItemRepository.save(CartItem.create(userId, 202L))
            
            When("일부 문제를 삭제하면") {
                val idsToDelete = listOf(cartItem1.id, cartItem2.id)
                cartItemRemover.removeCartItem(idsToDelete, userId)
                
                Then("해당 문제들만 삭제된다") {
                    val remainingItems = cartItemRepository.findByUserId(userId)
                    remainingItems shouldHaveSize 1
                    remainingItems[0].id shouldBe cartItem3.id
                }
            }
        }
        
        Given("장바구니에 문제가 담겨있을 때") {
            val userId = 101L
            val cartItem = cartItemRepository.save(CartItem.create(userId, 200L))
            
            When("모든 문제를 삭제하면") {
                cartItemRemover.removeCartItem(listOf(cartItem.id), userId)
                
                Then("장바구니가 비어있다") {
                    val remainingItems = cartItemRepository.findByUserId(userId)
                    remainingItems.shouldBeEmpty()
                }
            }
        }
    }
}