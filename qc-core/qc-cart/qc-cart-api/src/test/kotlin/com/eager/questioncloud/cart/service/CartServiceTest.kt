package com.eager.questioncloud.cart.service

import com.eager.questioncloud.cart.domain.CartItem
import com.eager.questioncloud.cart.infrastructure.repository.CartItemRepository
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CartServiceTest(
    @Autowired val cartService: CartService,
    @Autowired val cartItemRepository: CartItemRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI

    @MockBean
    lateinit var creatorQueryAPI: CreatorQueryAPI

    @MockBean
    lateinit var userQueryAPI: UserQueryAPI

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `장바구니 아이템을 추가할 수 있다`() {
        //given
        val userId = 100L
        val questionId = 200L

        given(questionQueryAPI.isAvailable(questionId))
            .willReturn(true)

        given(questionQueryAPI.isOwned(userId, questionId))
            .willReturn(false)

        //when
        cartService.appendCartItem(userId, questionId)

        //then
        val cartItems = cartItemRepository.findByUserId(userId)
        Assertions.assertThat(cartItems).hasSize(1)
        Assertions.assertThat(cartItems[0].questionId).isEqualTo(questionId)
    }

    @Test
    fun `장바구니 아이템 상세 목록을 조회할 수 있다`() {
        //given
        val userId = 101L
        val questionId1 = 201L
        val questionId2 = 202L
        val creatorId1 = 301L
        val creatorId2 = 302L
        val creatorUserId1 = 401L
        val creatorUserId2 = 402L

        val cartItem1 = CartItem.create(userId, questionId1)
        val cartItem2 = CartItem.create(userId, questionId2)
        cartItemRepository.save(cartItem1)
        cartItemRepository.save(cartItem2)

        given(questionQueryAPI.getQuestionInformation(any<List<Long>>()))
            .willReturn(
                listOf(
                    createQuestionInformation(questionId1, creatorId1, "문제1", "수학", "thumb1.jpg", 10000),
                    createQuestionInformation(questionId2, creatorId2, "문제2", "영어", "thumb2.jpg", 15000)
                )
            )

        given(creatorQueryAPI.getCreators(any()))
            .willReturn(
                listOf(
                    createCreatorQueryData(creatorUserId1, creatorId1),
                    createCreatorQueryData(creatorUserId2, creatorId2)
                )
            )

        given(userQueryAPI.getUsers(any()))
            .willReturn(
                listOf(
                    UserQueryData(creatorUserId1, "크리에이터1", "profile1.jpg", "creator1@test.com"),
                    UserQueryData(creatorUserId2, "크리에이터2", "profile2.jpg", "creator2@test.com")
                )
            )

        //when
        val result = cartService.getCartItemDetails(userId)

        //then
        Assertions.assertThat(result).hasSize(2)

        val titles = result.map { it.title }
        val creatorNames = result.map { it.creatorName }
        val prices = result.map { it.price }

        Assertions.assertThat(titles).containsExactlyInAnyOrder("문제1", "문제2")
        Assertions.assertThat(creatorNames).containsExactlyInAnyOrder("크리에이터1", "크리에이터2")
        Assertions.assertThat(prices).containsExactlyInAnyOrder(10000, 15000)
    }

    @Test
    fun `장바구니 아이템을 삭제할 수 있다`() {
        //given
        val userId = 102L
        val questionId1 = 203L
        val questionId2 = 204L
        val questionId3 = 205L

        val cartItem1 = CartItem.create(userId, questionId1)
        val cartItem2 = CartItem.create(userId, questionId2)
        val cartItem3 = CartItem.create(userId, questionId3)
        val savedCartItem1 = cartItemRepository.save(cartItem1)
        val savedCartItem2 = cartItemRepository.save(cartItem2)
        val savedCartItem3 = cartItemRepository.save(cartItem3)

        val idsToDelete = listOf(savedCartItem1.id, savedCartItem3.id)

        //when
        cartService.removeCartItem(idsToDelete, userId)

        //then
        val remainingCartItems = cartItemRepository.findByUserId(userId)
        Assertions.assertThat(remainingCartItems).hasSize(1)
        Assertions.assertThat(remainingCartItems[0].id).isEqualTo(savedCartItem2.id)
        Assertions.assertThat(remainingCartItems[0].questionId).isEqualTo(questionId2)
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
