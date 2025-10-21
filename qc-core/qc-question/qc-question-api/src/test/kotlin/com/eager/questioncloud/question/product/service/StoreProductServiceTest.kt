package com.eager.questioncloud.question.product.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.dto.QuestionInformation
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.enums.Subject
import com.eager.questioncloud.question.product.dto.StoreProductDetail
import com.eager.questioncloud.question.product.implement.StoreProductDetailReader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class StoreProductServiceTest(
    @Autowired val storeProductService: StoreProductService
) {
    @MockBean
    lateinit var storeProductDetailReader: StoreProductDetailReader
    
    private val userId = 1L
    private val questionId = 100L
    private val creatorId = 101L
    
    @Test
    fun `필터링된 전체 문제 개수를 조회할 수 있다`() {
        //given
        val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
        val expectedCount = 10
        
        given(storeProductDetailReader.count(questionFilter))
            .willReturn(expectedCount)
        
        //when
        val result = storeProductService.getTotalFiltering(questionFilter)
        
        //then
        Assertions.assertThat(result).isEqualTo(10)
    }
    
    @Test
    fun `필터링된 문제 목록을 조회할 수 있다`() {
        //given
        val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
        val pagingInformation = PagingInformation(0, 10)
        
        val questionInformation = QuestionInformation(
            id = questionId,
            creatorId = creatorId,
            title = "수학 문제 1",
            description = "엄청 어려운 문제임",
            subject = Subject.Mathematics,
            parentCategory = "수학",
            childCategory = "미적분",
            thumbnail = "thumbnail.jpg",
            questionLevel = QuestionLevel.LEVEL3,
            price = 1000,
            promotionName = "300번째 문제 행사",
            promotionPrice = 300,
            rate = 4.5
        )
        
        val storeProductDetail = StoreProductDetail(
            questionContent = questionInformation,
            creator = "수학선생님",
            isOwned = false
        )
        
        val expectedResult = listOf(storeProductDetail)
        
        given(storeProductDetailReader.getStoreProductDetails(userId, questionFilter, pagingInformation))
            .willReturn(expectedResult)
        
        //when
        val result = storeProductService.getQuestionListByFiltering(userId, questionFilter, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(1)
        Assertions.assertThat(result[0].questionContent.title).isEqualTo("수학 문제 1")
        Assertions.assertThat(result[0].creator).isEqualTo("수학선생님")
        Assertions.assertThat(result[0].isOwned).isFalse()
    }
    
    @Test
    fun `문제 정보를 조회할 수 있다`() {
        //given
        val questionInformation = QuestionInformation(
            id = questionId,
            creatorId = creatorId,
            title = "고급 수학 문제",
            description = "엄청 어려운 문제임",
            subject = Subject.Mathematics,
            parentCategory = "수학",
            childCategory = "선형대수",
            thumbnail = "advanced_thumbnail.jpg",
            questionLevel = QuestionLevel.LEVEL5,
            price = 2000,
            promotionName = "300번째 문제 행사",
            promotionPrice = 300,
            rate = 4.8
        )
        
        val storeProductDetail = StoreProductDetail(
            questionContent = questionInformation,
            creator = "고급수학선생님",
            isOwned = true
        )
        
        given(storeProductDetailReader.getStoreProductDetail(questionId, userId))
            .willReturn(storeProductDetail)
        
        //when
        val result = storeProductService.getQuestionInformation(questionId, userId)
        
        //then
        Assertions.assertThat(result.questionContent.id).isEqualTo(questionId)
        Assertions.assertThat(result.questionContent.title).isEqualTo("고급 수학 문제")
        Assertions.assertThat(result.creator).isEqualTo("고급수학선생님")
        Assertions.assertThat(result.isOwned).isTrue()
    }
}
