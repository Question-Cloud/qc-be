package com.eager.questioncloud.cart.service

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
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
            val userId = 101L
            val questionId1 = 201L
            val questionId2 = 202L
            val creatorId1 = 301L
            val creatorId2 = 302L
            val creatorUserId1 = 401L
            val creatorUserId2 = 402L
            
            cartItemRepository.save(CartItem.create(userId, questionId1))
            cartItemRepository.save(CartItem.create(userId, questionId2))
            
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns listOf(
                QuestionInformationQueryResult(
                    id = questionId1, creatorId = creatorId1, title = "문제1", subject = "수학",
                    parentCategory = "수학", childCategory = "수학_하위",
                    thumbnail = "thumb1.jpg", questionLevel = "중급", price = 10000, rate = 4.5
                ),
                QuestionInformationQueryResult(
                    id = questionId2, creatorId = creatorId2, title = "문제2", subject = "영어",
                    parentCategory = "영어", childCategory = "영어_하위",
                    thumbnail = "thumb2.jpg", questionLevel = "중급", price = 15000, rate = 4.5
                )
            )
            
            every { creatorQueryAPI.getCreators(any()) } returns listOf(
                CreatorQueryData(creatorUserId1, creatorId1, "전체", 4.5, 100, 500),
                CreatorQueryData(creatorUserId2, creatorId2, "전체", 4.5, 100, 500)
            )
            
            every { userQueryAPI.getUsers(any()) } returns listOf(
                UserQueryData(creatorUserId1, "크리에이터1", "profile1.jpg", "creator1@test.com"),
                UserQueryData(creatorUserId2, "크리에이터2", "profile2.jpg", "creator2@test.com")
            )
            
            val result = cartService.getCartItemDetails(userId)
            
            result shouldHaveSize 2
            result.map { it.title } shouldContainExactlyInAnyOrder listOf("문제1", "문제2")
            result.map { it.creatorName } shouldContainExactlyInAnyOrder listOf("크리에이터1", "크리에이터2")
            result.map { it.price } shouldContainExactlyInAnyOrder listOf(10000, 15000)
        }
        
        test("장바구니에서 문제를 삭제할 수 있다") {
            val userId = 102L
            val questionId1 = 203L
            val questionId2 = 204L
            val questionId3 = 205L
            
            val savedItem1 = cartItemRepository.save(CartItem.create(userId, questionId1))
            val savedItem2 = cartItemRepository.save(CartItem.create(userId, questionId2))
            val savedItem3 = cartItemRepository.save(CartItem.create(userId, questionId3))
            
            val idsToDelete = listOf(savedItem1.id, savedItem3.id)
            
            cartService.removeCartItem(idsToDelete, userId)
            
            val remainingItems = cartItemRepository.findByUserId(userId)
            remainingItems shouldHaveSize 1
            remainingItems[0].id shouldBe savedItem2.id
            remainingItems[0].questionId shouldBe questionId2
        }
    }
}
