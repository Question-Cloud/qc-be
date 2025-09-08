package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class QuestionOrderServiceTest(
    @Autowired val questionOrderService: QuestionOrderService,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `문제 주문을 생성할 수 있다`() {
        //given
        val userId = 100L
        val questionId1 = 200L
        val questionId2 = 201L
        val questionIds = listOf(questionId1, questionId2)
        
        given(questionQueryAPI.isOwned(userId, questionIds))
            .willReturn(false)
        
        given(questionQueryAPI.getQuestionInformation(questionIds))
            .willReturn(
                listOf(
                    createQuestionInformation(questionId1, "수학 문제", 10000),
                    createQuestionInformation(questionId2, "영어 문제", 15000)
                )
            )
        
        //when
        val result = questionOrderService.generateQuestionOrder(userId, questionIds)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.orderId).isNotBlank()
        Assertions.assertThat(result.items).hasSize(2)
        Assertions.assertThat(result.calcAmount()).isEqualTo(25000)
        Assertions.assertThat(result.questionIds).containsExactlyInAnyOrder(questionId1, questionId2)
    }
    
    private fun createQuestionInformation(questionId: Long, title: String, price: Int): QuestionInformationQueryResult {
        return QuestionInformationQueryResult(
            id = questionId,
            creatorId = 300L,
            title = title,
            subject = "일반",
            parentCategory = "일반",
            childCategory = "일반_하위",
            thumbnail = "thumbnail.jpg",
            questionLevel = "중급",
            price = price,
            rate = 4.0
        )
    }
}
