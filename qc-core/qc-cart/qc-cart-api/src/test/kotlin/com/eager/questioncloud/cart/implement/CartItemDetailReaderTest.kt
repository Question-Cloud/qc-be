package com.eager.questioncloud.cart.implement

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
class CartItemDetailReaderTest(
    private val cartItemDetailReader: CartItemDetailReader,
    private val cartItemRepository: CartItemRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockkBean
    private lateinit var creatorQueryAPI: CreatorQueryAPI
    
    @MockkBean
    private lateinit var userQueryAPI: UserQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("장바구니에 문제가 담겨 있을 때") {
            val userId = 100L
            val question1Id = 200L
            val question2Id = 201L
            
            val question1CreatorId = 1L
            val question2CreatorId = 2L
            
            val question1CreatorUserId = 1L
            val question2CreatorUserId = 2L
            
            val savedCartItem1 = cartItemRepository.save(CartItem.create(userId, question1Id))
            val savedCartItem2 = cartItemRepository.save(CartItem.create(userId, question2Id))
            
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
            
            When("장바구니를 조회하면") {
                every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns listOf(
                    question1QueryData, question2QueryData,
                )
                
                every { creatorQueryAPI.getCreators(any()) } returns listOf(
                    question1CreatorData, question2CreatorData,
                )
                
                every { userQueryAPI.getUsers(any()) } returns listOf(
                    question1CreatorUserData, question2CreatorUserData
                )
                
                val result = cartItemDetailReader.getCartItemDetails(userId)
                
                Then("장바구니 아이템들의 상세 정보가 반환된다") {
                    result shouldHaveSize 2
                    
                    val detail1 = result.find { it.questionId == question1Id }!!
                    detail1.id shouldBe savedCartItem1.id
                    detail1.title shouldBe question1QueryData.title
                    detail1.creatorName shouldBe question1CreatorUserData.name
                    detail1.subject shouldBe question1QueryData.subject
                    detail1.price shouldBe question1QueryData.price
                    
                    val detail2 = result.find { it.questionId == question2Id }!!
                    detail2.id shouldBe savedCartItem2.id
                    detail2.title shouldBe question2QueryData.title
                    detail2.creatorName shouldBe question2CreatorUserData.name
                    detail2.subject shouldBe question2QueryData.subject
                    detail2.price shouldBe question2QueryData.price
                }
            }
        }
    }
}
