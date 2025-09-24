package com.eager.questioncloud.cart.implement

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.repository.CartItemRepository
import com.eager.questioncloud.cart.scenario.CartItemScenario
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
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
            val userId = 1L
            val cartItemCount = 10
            val scenario = CartItemScenario.create(cartItemCount)
            val savedCartItems = scenario.cartItemQuestionIds.map { cartItemRepository.save(CartItem.create(userId, it)) }
            
            every { questionQueryAPI.getQuestionInformation(any<List<Long>>()) } returns scenario.questionInformationQueryResults
            every { creatorQueryAPI.getCreators(any()) } returns scenario.creatorQueryDatas
            every { userQueryAPI.getUsers(any()) } returns scenario.creatorUserQueryDatas
            
            When("장바구니를 조회하면") {
                val result = cartItemDetailReader.getCartItemDetails(userId)
                
                Then("장바구니 아이템들의 상세 정보가 반환된다") {
                    result shouldHaveSize cartItemCount
                    
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
            }
        }
    }
}
