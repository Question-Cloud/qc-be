package com.eager.questioncloud.cart.service

import com.eager.questioncloud.cart.dto.CartItemDetail
import com.eager.questioncloud.cart.implement.CartItemAppender
import com.eager.questioncloud.cart.implement.CartItemDetailReader
import com.eager.questioncloud.cart.repository.CartItemRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.*

class CartServiceTest : BehaviorSpec() {
    private val cartItemAppender = mockk<CartItemAppender>()
    private val cartItemDetailReader = mockk<CartItemDetailReader>()
    private val cartItemRepository = mockk<CartItemRepository>()
    
    private val cartService = CartService(
        cartItemAppender,
        cartItemDetailReader,
        cartItemRepository
    )
    
    init {
        afterEach {
            clearMocks(cartItemAppender, cartItemDetailReader, cartItemRepository)
        }
        
        Given("장바구니에 문제 추가") {
            val userId = 1L
            val questionId = 1L
            
            justRun { cartItemAppender.append(userId, questionId) }
            
            When("사용자가 장바구니에 문제를 추가하면") {
                cartService.appendCartItem(userId, questionId)
                
                Then("문제가 장바구니에 추가된다") {
                    verify(exactly = 1) { cartItemAppender.append(userId, questionId) }
                }
            }
        }
        
        Given("장바구니 아이템 상세 목록 조회") {
            val userId = 1L
            
            val cartItemDetails = listOf(
                CartItemDetail(
                    id = 1L,
                    questionId = 1L,
                    title = "문제 제목 1",
                    thumbnail = "thumbnail1.jpg",
                    creatorName = "크리에이터 1",
                    subject = "수학",
                    price = 10000
                ),
                CartItemDetail(
                    id = 2L,
                    questionId = 2L,
                    title = "문제 제목 2",
                    thumbnail = "thumbnail2.jpg",
                    creatorName = "크리에이터 2",
                    subject = "영어",
                    price = 15000
                )
            )
            
            every { cartItemDetailReader.getCartItemDetails(userId) } returns cartItemDetails
            
            When("장바구니 아이템 상세 목록을 조회하면") {
                val result = cartService.getCartItemDetails(userId)
                
                Then("장바구니 아이템 상세 목록이 반환된다") {
                    result shouldHaveSize 2
                    result[0].title shouldBe cartItemDetails[0].title
                    result[0].creatorName shouldBe cartItemDetails[0].creatorName
                    result[1].title shouldBe cartItemDetails[1].title
                    result[1].creatorName shouldBe cartItemDetails[1].creatorName
                    
                    verify(exactly = 1) { cartItemDetailReader.getCartItemDetails(userId) }
                }
            }
        }
        
        Given("장바구니에서 문제 삭제") {
            val userId = 1L
            val idsToDelete = listOf(1L, 2L, 3L)
            
            justRun { cartItemRepository.deleteByIdInAndUserId(idsToDelete, userId) }
            
            When("장바구니에서 문제를 삭제하면") {
                cartService.removeCartItem(idsToDelete, userId)
                
                Then("문제가 장바구니에서 삭제된다") {
                    verify(exactly = 1) { cartItemRepository.deleteByIdInAndUserId(idsToDelete, userId) }
                }
            }
        }
    }
}