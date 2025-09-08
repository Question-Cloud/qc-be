package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
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
class QuestionOrderGeneratorTest(
    @Autowired val questionOrderGenerator: QuestionOrderGenerator,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `Question 주문을 생성할 수 있다`() {
        // given
        val userId = 1L
        val questionIds = listOf(1L, 2L, 3L)
        given(questionQueryAPI.isOwned(any(), any<List<Long>>())).willReturn(false)
        given(questionQueryAPI.getQuestionInformation(any<List<Long>>()))
            .willReturn(
                listOf(
                    QuestionInformationQueryResult(
                        id = 1L,
                        creatorId = 1L,
                        title = "테스트 문제",
                        subject = "수학",
                        parentCategory = "수학",
                        childCategory = "미적",
                        thumbnail = "thumbnail.jpg",
                        questionLevel = "중급",
                        price = 1000,
                        rate = 4.5
                    ), QuestionInformationQueryResult(
                        id = 2L,
                        creatorId = 1L,
                        title = "테스트 문제",
                        subject = "수학",
                        parentCategory = "수학",
                        childCategory = "미적",
                        thumbnail = "thumbnail.jpg",
                        questionLevel = "중급",
                        price = 1000,
                        rate = 4.5
                    ), QuestionInformationQueryResult(
                        id = 3L,
                        creatorId = 1L,
                        title = "테스트 문제",
                        subject = "수학",
                        parentCategory = "수학",
                        childCategory = "미적",
                        thumbnail = "thumbnail.jpg",
                        questionLevel = "중급",
                        price = 1000,
                        rate = 4.5
                    )
                )
            )
        
        // when
        val questionOrder = questionOrderGenerator.generateQuestionOrder(userId, questionIds)
        
        // then
        Assertions.assertThat(questionOrder.questionIds).containsExactlyInAnyOrderElementsOf(questionIds)
    }
    
    @Test
    fun `비활성화 된 Question을 포함한 주문을 생성할 수 없다`() {
        // given
        val userId = 1L
        val questionIds = listOf(1L, 2L, 3L) // 3L is unAvailable
        given(questionQueryAPI.isOwned(any(), any<List<Long>>())).willReturn(false)
        given(questionQueryAPI.getQuestionInformation(any<List<Long>>())).willReturn(
            listOf(
                QuestionInformationQueryResult(
                    id = 1L,
                    creatorId = 1L,
                    title = "테스트 문제",
                    subject = "수학",
                    parentCategory = "수학",
                    childCategory = "미적",
                    thumbnail = "thumbnail.jpg",
                    questionLevel = "중급",
                    price = 1000,
                    rate = 4.5
                ),
                QuestionInformationQueryResult(
                    id = 2L,
                    creatorId = 1L,
                    title = "테스트 문제",
                    subject = "수학",
                    parentCategory = "수학",
                    childCategory = "미적",
                    thumbnail = "thumbnail.jpg",
                    questionLevel = "중급",
                    price = 1000,
                    rate = 4.5
                ),
//                QuestionInformationQueryResult(
//                    id = 3L,
//                    creatorId = 1L,
//                    title = "테스트 문제",
//                    subject = "수학",
//                    parentCategory = "수학",
//                    childCategory = "미적",
//                    thumbnail = "thumbnail.jpg",
//                    questionLevel = "중급",
//                    price = 1000,
//                    rate = 4.5
//                )
            )
        
        )
        //when then
        Assertions.assertThatThrownBy {
            questionOrderGenerator.generateQuestionOrder(
                userId, questionIds
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.UNAVAILABLE_QUESTION)
    }
    
    @Test
    fun `이미 구매한 Question은 주문에 포함할 수 없다`() {
        // given
        val userId = 1L
        val alreadyOwnedQuestionId = 1L
        val normalQuestionId = 2L
        
        given(questionQueryAPI.isOwned(any(), any<List<Long>>())).willReturn(true)
        
        // when then
        Assertions.assertThatThrownBy {
            questionOrderGenerator.generateQuestionOrder(
                userId,
                listOf(alreadyOwnedQuestionId, normalQuestionId)
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_OWN_QUESTION)
    }
}