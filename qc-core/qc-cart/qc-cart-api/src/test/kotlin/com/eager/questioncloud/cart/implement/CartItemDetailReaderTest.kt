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
            val questionId1 = 200L
            val questionId2 = 201L
            val creatorId1 = 300L
            val creatorId2 = 301L
            val creatorUserId1 = 400L
            val creatorUserId2 = 401L
            
            val cartItem1 = CartItem.create(userId, questionId1)
            val cartItem2 = CartItem.create(userId, questionId2)
            val savedCartItem1 = cartItemRepository.save(cartItem1)
            val savedCartItem2 = cartItemRepository.save(cartItem2)
            
            When("장바구니를 조회하면") {
                every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns listOf(
                    createQuestionInformation(questionId1, creatorId1, "첫 번째 문제", "수학", "thumbnail1.jpg", 10000),
                    createQuestionInformation(questionId2, creatorId2, "두 번째 문제", "영어", "thumbnail2.jpg", 15000)
                )
                
                every { creatorQueryAPI.getCreators(any()) } returns listOf(
                    createCreatorQueryData(creatorUserId1, creatorId1),
                    createCreatorQueryData(creatorUserId2, creatorId2)
                )
                
                every { userQueryAPI.getUsers(any()) } returns listOf(
                    UserQueryData(creatorUserId1, "크리에이터1", "profile1.jpg", "creator1@test.com"),
                    UserQueryData(creatorUserId2, "크리에이터2", "profile2.jpg", "creator2@test.com")
                )
                
                val result = cartItemDetailReader.getCartItemDetails(userId)
                
                Then("장바구니 아이템들의 상세 정보가 반환된다") {
                    result shouldHaveSize 2
                    
                    val detail1 = result.find { it.questionId == questionId1 }!!
                    detail1.id shouldBe savedCartItem1.id
                    detail1.title shouldBe "첫 번째 문제"
                    detail1.creatorName shouldBe "크리에이터1"
                    detail1.subject shouldBe "수학"
                    detail1.price shouldBe 10000
                    
                    val detail2 = result.find { it.questionId == questionId2 }!!
                    detail2.id shouldBe savedCartItem2.id
                    detail2.title shouldBe "두 번째 문제"
                    detail2.creatorName shouldBe "크리에이터2"
                    detail2.subject shouldBe "영어"
                    detail2.price shouldBe 15000
                }
            }
        }
    }
    
    private fun createQuestionInformation(
        questionId: Long,
        creatorId: Long,
        title: String,
        subject: String,
        thumbnail: String,
        price: Int
    ): QuestionInformationQueryResult {
        return QuestionInformationQueryResult(
            id = questionId,
            creatorId = creatorId,
            title = title,
            subject = subject,
            parentCategory = subject,
            childCategory = "${subject}_하위",
            thumbnail = thumbnail,
            questionLevel = "중급",
            price = price,
            rate = 4.5
        )
    }
    
    private fun createCreatorQueryData(userId: Long, creatorId: Long): CreatorQueryData {
        return CreatorQueryData(
            userId = userId,
            creatorId = creatorId,
            mainSubject = "전체",
            rate = 4.5,
            sales = 100,
            subscriberCount = 500
        )
    }
}
