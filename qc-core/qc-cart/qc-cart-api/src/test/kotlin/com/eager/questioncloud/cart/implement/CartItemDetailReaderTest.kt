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
class CartItemDetailReaderTest(
    @Autowired val cartItemDetailReader: CartItemDetailReader,
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
    fun `장바구니 아이템 상세 정보를 조회할 수 있다`() {
        //given
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
        
        given(questionQueryAPI.getQuestionInformation(any<List<Long>>()))
            .willReturn(
                listOf(
                    createQuestionInformation(questionId1, creatorId1, "첫 번째 문제", "수학", "thumbnail1.jpg", 10000),
                    createQuestionInformation(questionId2, creatorId2, "두 번째 문제", "영어", "thumbnail2.jpg", 15000)
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
        val result = cartItemDetailReader.getCartItemDetails(userId)
        
        //then
        Assertions.assertThat(result).hasSize(2)
        
        val detail1 = result.find { it.questionId == questionId1 }
        Assertions.assertThat(detail1).isNotNull
        Assertions.assertThat(detail1!!.id).isEqualTo(savedCartItem1.id)
        Assertions.assertThat(detail1.title).isEqualTo("첫 번째 문제")
        Assertions.assertThat(detail1.thumbnail).isEqualTo("thumbnail1.jpg")
        Assertions.assertThat(detail1.creatorName).isEqualTo("크리에이터1")
        Assertions.assertThat(detail1.subject).isEqualTo("수학")
        Assertions.assertThat(detail1.price).isEqualTo(10000)
        
        val detail2 = result.find { it.questionId == questionId2 }
        Assertions.assertThat(detail2).isNotNull
        Assertions.assertThat(detail2!!.id).isEqualTo(savedCartItem2.id)
        Assertions.assertThat(detail2.title).isEqualTo("두 번째 문제")
        Assertions.assertThat(detail2.thumbnail).isEqualTo("thumbnail2.jpg")
        Assertions.assertThat(detail2.creatorName).isEqualTo("크리에이터2")
        Assertions.assertThat(detail2.subject).isEqualTo("영어")
        Assertions.assertThat(detail2.price).isEqualTo(15000)
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
