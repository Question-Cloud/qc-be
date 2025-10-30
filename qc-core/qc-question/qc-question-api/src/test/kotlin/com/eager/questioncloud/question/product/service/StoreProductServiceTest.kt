package com.eager.questioncloud.question.product.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.dto.QuestionInformation
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.enums.Subject
import com.eager.questioncloud.question.product.dto.StoreProductDetail
import com.eager.questioncloud.question.product.implement.StoreProductDetailReader
import com.eager.questioncloud.question.repository.QuestionCategoryRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk

class StoreProductServiceTest : BehaviorSpec() {
    private val storeProductDetailReader = mockk<StoreProductDetailReader>()
    private val questionCategoryRepository = mockk<QuestionCategoryRepository>()
    private val storeProductService = StoreProductService(storeProductDetailReader, questionCategoryRepository)

    private val userId = 1L
    private val questionId = 100L
    private val creatorId = 101L

    init {
        afterEach {
            clearMocks(storeProductDetailReader, questionCategoryRepository)
        }

        Given("필터가 적용된 경우") {
            val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
            val expectedCount = 10

            every { storeProductDetailReader.count(questionFilter) } returns expectedCount

            When("필터링된 전체 문제 개수를 조회하면") {
                val result = storeProductService.getTotalFiltering(questionFilter)

                Then("필터링된 문제 개수가 반환된다") {
                    result shouldBe 10
                }
            }
        }

        Given("필터와 페이징 정보가 주어진 경우") {
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

            every {
                storeProductDetailReader.getStoreProductDetails(userId, questionFilter, pagingInformation)
            } returns expectedResult

            When("필터링된 문제 목록을 조회하면") {
                val result = storeProductService.getQuestionListByFiltering(userId, questionFilter, pagingInformation)

                Then("문제 목록과 크리에이터 정보, 소유 여부가 반환된다") {
                    result shouldHaveSize 1
                    result[0].questionContent.title shouldBe "수학 문제 1"
                    result[0].creator shouldBe "수학선생님"
                    result[0].isOwned shouldBe false
                }
            }
        }

        Given("특정 문제가 존재하는 경우") {
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

            every { storeProductDetailReader.getStoreProductDetail(questionId, userId) } returns storeProductDetail

            When("문제 정보를 조회하면") {
                val result = storeProductService.getQuestionInformation(questionId, userId)

                Then("문제 상세 정보와 크리에이터 정보, 소유 여부가 반환된다") {
                    result.questionContent.id shouldBe questionId
                    result.questionContent.title shouldBe "고급 수학 문제"
                    result.creator shouldBe "고급수학선생님"
                    result.isOwned shouldBe true
                }
            }
        }
    }
}
