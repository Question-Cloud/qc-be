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
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
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
            val userId = 100L
            val question1Id = 200L
            val question2Id = 201L
            
            val question1CreatorId = 1L
            val question2CreatorId = 2L
            
            val question1CreatorUserId = 1L
            val question2CreatorUserId = 2L
            
            cartItemRepository.save(CartItem.create(userId, question1Id))
            cartItemRepository.save(CartItem.create(userId, question2Id))
            
            val question1QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, question1Id)
                .set(QuestionInformationQueryResult::creatorId, question1CreatorId)
                .sample()
            
            val question2QueryData = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                .set(QuestionInformationQueryResult::id, question2Id)
                .set(QuestionInformationQueryResult::creatorId, question2CreatorId)
                .sample()
            
            val question1CreatorData = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorQueryData>()
                .set(CreatorQueryData::userId, question1CreatorUserId)
                .set(CreatorQueryData::creatorId, question1CreatorId)
                .sample()
            
            val question2CreatorData = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorQueryData>()
                .set(CreatorQueryData::userId, question2CreatorUserId)
                .set(CreatorQueryData::creatorId, question2CreatorId)
                .sample()
            
            val question1CreatorUserData =
                Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>().set(UserQueryData::userId, question1CreatorUserId).sample()
            
            val question2CreatorUserData =
                Fixture.fixtureMonkey.giveMeKotlinBuilder<UserQueryData>().set(UserQueryData::userId, question2CreatorUserId).sample()
            
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns listOf(
                question1QueryData, question2QueryData,
            )
            
            every { creatorQueryAPI.getCreators(any()) } returns listOf(
                question1CreatorData, question2CreatorData,
            )
            
            every { userQueryAPI.getUsers(any()) } returns listOf(
                question1CreatorUserData, question2CreatorUserData
            )
            
            val result = cartService.getCartItemDetails(userId)
            
            result shouldHaveSize 2
            result.map { it.title } shouldContainExactlyInAnyOrder listOf(question1QueryData.title, question2QueryData.title)
            result.map { it.creatorName } shouldContainExactlyInAnyOrder listOf(
                question1CreatorUserData.name,
                question2CreatorUserData.name
            )
            result.map { it.price } shouldContainExactlyInAnyOrder listOf(question1QueryData.price, question2QueryData.price)
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
