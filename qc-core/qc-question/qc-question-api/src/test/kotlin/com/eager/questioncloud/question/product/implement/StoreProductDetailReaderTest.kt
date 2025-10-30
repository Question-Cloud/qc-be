package com.eager.questioncloud.question.product.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.fixture.QuestionFixtureHelper
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import com.eager.questioncloud.question.repository.QuestionRepository
import com.eager.questioncloud.question.repository.UserQuestionRepository
import com.eager.questioncloud.test.utils.DBCleaner
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class StoreProductDetailReaderTest(
    private val storeProductDetailReader: StoreProductDetailReader,
    private val questionRepository: QuestionRepository,
    private val userQuestionRepository: UserQuestionRepository,
    private val questionMetadataRepository: QuestionMetadataRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {

    @MockkBean
    private lateinit var creatorQueryAPI: CreatorQueryAPI

    @MockkBean
    private lateinit var userQueryAPI: UserQueryAPI

    private val userId = 1L
    private val creatorId1 = 101L
    private val creatorId2 = 102L
    private val creatorUserId1 = 201L
    private val creatorUserId2 = 202L

    init {
        afterTest {
            dbCleaner.cleanUp()
        }

        Given("여러 문제가 있고 사용자가 일부를 소유하고 있을 때") {
            val question1 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL3,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
            val question2 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId2,
                questionLevel = QuestionLevel.LEVEL4,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            saveUserQuestion(userId, question1.id)

            val creatorQueryData1 = CreatorQueryData(creatorUserId1, creatorId1, "수학", "zzzz", 4.5, 100, 50)
            val creatorQueryData2 = CreatorQueryData(creatorUserId2, creatorId2, "영어", "zzzz", 4.8, 80, 30)
            val userQueryData1 = UserQueryData(creatorUserId1, "수학선생님", "math_profile.jpg", "math@test.com")
            val userQueryData2 = UserQueryData(creatorUserId2, "영어선생님", "english_profile.jpg", "english@test.com")

            every { creatorQueryAPI.getCreators(any()) } returns listOf(creatorQueryData1, creatorQueryData2)
            every { userQueryAPI.getUsers(any()) } returns listOf(userQueryData1, userQueryData2)

            val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
            val pagingInformation = PagingInformation(0, 10)

            When("문제 목록을 조회하면") {
                val storeProductDetails =
                    storeProductDetailReader.getStoreProductDetails(userId, questionFilter, pagingInformation)

                Then("모든 문제와 소유 여부가 포함된 상품 정보가 반환된다") {
                    storeProductDetails shouldHaveSize 2

                    val product1 = storeProductDetails.find { it.questionContent.id == question1.id }
                    product1 shouldNotBe null
                    product1!!.creator shouldBe "수학선생님"
                    product1.isOwned shouldBe true

                    val product2 = storeProductDetails.find { it.questionContent.id == question2.id }
                    product2 shouldNotBe null
                    product2!!.creator shouldBe "영어선생님"
                    product2.isOwned shouldBe false
                }
            }
        }

        Given("사용자가 소유한 문제가 있을 때") {
            val question = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL3,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            saveUserQuestion(userId, question.id)

            val creatorQueryData = CreatorQueryData(creatorUserId1, creatorId1, "수학전문가", "zzzz", 4.7, 200, 100)
            val userQueryData = UserQueryData(creatorUserId1, "김수학", "math_expert.jpg", "math@example.com")

            every { creatorQueryAPI.getCreator(creatorId1) } returns creatorQueryData
            every { userQueryAPI.getUser(creatorUserId1) } returns userQueryData

            When("상품 상세 정보를 조회하면") {
                val storeProductDetail = storeProductDetailReader.getStoreProductDetail(question.id, userId)

                Then("문제와 크리에이터 정보, 소유 여부가 반환된다") {
                    storeProductDetail.questionContent.id shouldBe question.id
                    storeProductDetail.creator shouldBe "김수학"
                    storeProductDetail.isOwned shouldBe true
                }
            }
        }

        Given("여러 문제가 등록되어 있을 때") {
            val questions = listOf(
                QuestionFixtureHelper.createQuestion(
                    creatorId1,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                ),
                QuestionFixtureHelper.createQuestion(
                    creatorId1,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                ),
                QuestionFixtureHelper.createQuestion(
                    creatorId2,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                ),
                QuestionFixtureHelper.createQuestion(
                    creatorId2,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                ),
                QuestionFixtureHelper.createQuestion(
                    creatorId2,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                )
            )

            val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)

            When("필터링된 스토어 상품 개수를 조회하면") {
                val count = storeProductDetailReader.count(questionFilter)

                Then("전체 문제 개수가 반환된다") {
                    count shouldBe 5
                }
            }
        }

        Given("특정 크리에이터의 문제가 여러 개 있을 때") {
            val creator1Questions = listOf(
                QuestionFixtureHelper.createQuestion(
                    creatorId1,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                ),
                QuestionFixtureHelper.createQuestion(
                    creatorId1,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                )
            )
            val creator2Questions = listOf(
                QuestionFixtureHelper.createQuestion(
                    creatorId2,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                )
            )

            val creatorQueryData1 = CreatorQueryData(creatorUserId1, creatorId1, "전문크리에이터", "zzzz", 4.6, 150, 75)
            val userQueryData1 = UserQueryData(creatorUserId1, "이전문", "expert.jpg", "expert@test.com")

            every { creatorQueryAPI.getCreators(any()) } returns listOf(creatorQueryData1)
            every { userQueryAPI.getUsers(any()) } returns listOf(userQueryData1)

            val questionFilter = QuestionFilter(creatorId = creatorId1, sort = QuestionSortType.Latest)
            val pagingInformation = PagingInformation(0, 10)

            When("특정 크리에이터로 필터링하여 조회하면") {
                val storeProductDetails =
                    storeProductDetailReader.getStoreProductDetails(userId, questionFilter, pagingInformation)

                Then("해당 크리에이터의 문제만 반환된다") {
                    storeProductDetails shouldHaveSize 2
                    storeProductDetails.forEach { product ->
                        product.questionContent.creatorId shouldBe creatorId1
                        product.creator shouldBe "이전문"
                        product.isOwned shouldBe false
                    }
                }
            }
        }
    }

    private fun saveUserQuestion(userId: Long, questionId: Long) {
        val userQuestion = UserQuestion.create(userId, questionId)
        userQuestionRepository.saveAll(listOf(userQuestion))
    }
}