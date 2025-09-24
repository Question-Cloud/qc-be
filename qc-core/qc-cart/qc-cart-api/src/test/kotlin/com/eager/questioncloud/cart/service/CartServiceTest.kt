package com.eager.questioncloud.cart.service

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.cart.scenario.CartItemScenario
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class CartServiceTest(
    private val cartService: CartService,
    private val cartItemRepository: CartItemRepository,
    private val dbCleaner: DBCleaner,
) : FunSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockkBean
    private lateinit var creatorQueryAPI: CreatorQueryAPI
    
    @MockkBean
    private lateinit var userQueryAPI: UserQueryAPI
    
    init {
        afterTest { dbCleaner.cleanUp() }
        
        test("장바구니에 문제를 추가할 수 있다") {
            val userId = 100L
            val questionId = 200L
            
            every { questionQueryAPI.isAvailable(questionId) } returns true
            every { questionQueryAPI.isOwned(userId, questionId) } returns false
            
            cartService.appendCartItem(userId, questionId)
            
            val cartItems = cartItemRepository.findByUserId(userId)
            cartItems shouldHaveSize 1
            cartItems[0].questionId shouldBe questionId
        }
        
        test("장바구니 아이템 상세 목록을 조회할 수 있다") {
            val userId = 1L
            val cartItemCount = 10
            val scenario = CartItemScenario.create(cartItemCount)
            val savedCartItems = scenario.cartItemQuestionIds.map { cartItemRepository.save(CartItem.create(userId, it)) }
            
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns scenario.questionInformationQueryResults
            every { creatorQueryAPI.getCreators(any()) } returns scenario.creatorQueryDatas
            every { userQueryAPI.getUsers(any()) } returns scenario.creatorUserQueryDatas
            
            val result = cartService.getCartItemDetails(userId)
            
            result shouldHaveSize 10
            
            scenario.cartItemQuestionIds.forEachIndexed { index, questionId ->
                val detail = result.find { it.questionId == questionId }!!
                val expectedQuestion = scenario.questionInformationQueryResults[index]
                val expectedCreatorUser = scenario.creatorUserQueryDatas[index]
                val expectedCartItem = savedCartItems[index]
                
                detail.id shouldBe expectedCartItem.id
                detail.title shouldBe expectedQuestion.title
                detail.creatorName shouldBe expectedCreatorUser.name
                detail.subject shouldBe expectedQuestion.subject
                detail.price shouldBe expectedQuestion.price
            }
        }
        
        test("장바구니에서 문제를 삭제할 수 있다") {
            val userId = 1L
            val beforeCartItemCount = 5
            val scenario = CartItemScenario.create(beforeCartItemCount)
            scenario.cartItemQuestionIds.forEach { cartItemRepository.save(CartItem.create(userId, it)) }
            val idsToDelete = listOf(scenario.cartItemQuestionIds[0], scenario.cartItemQuestionIds[1])
            
            cartService.removeCartItem(idsToDelete, userId)
            
            val remainingItems = cartItemRepository.findByUserId(userId)
            remainingItems shouldHaveSize beforeCartItemCount - idsToDelete.size
        }
    }
}
