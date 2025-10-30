package com.eager.questioncloud.question.api.internal

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionType
import com.eager.questioncloud.question.fixture.QuestionFixtureHelper
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import com.eager.questioncloud.question.repository.QuestionRepository
import com.eager.questioncloud.question.repository.UserQuestionRepository
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionQueryAPIImplTest(
    private val questionQueryAPI: QuestionQueryAPIImpl,
    private val questionRepository: QuestionRepository,
    private val userQuestionRepository: UserQuestionRepository,
    private val questionMetadataRepository: QuestionMetadataRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {

    private val userId = 1L
    private val creatorId1 = 101L
    private val creatorId2 = 102L

    init {
        afterTest {
            dbCleaner.cleanUp()
        }

        Given("문제가 존재할 때") {
            val question = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL3,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            When("문제 정보를 조회하면") {
                val result = questionQueryAPI.getQuestionInformation(question.id)

                Then("문제 정보가 반환된다") {
                    result.id shouldBe question.id
                    result.creatorId shouldBe creatorId1
                    result.questionLevel shouldBe "LEVEL3"
                    result.price shouldBe 1000
                }
            }
        }

        Given("여러 문제가 존재할 때") {
            val question1 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
            val question2 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId2,
                questionLevel = QuestionLevel.LEVEL5,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            val questionIds = listOf(question1.id, question2.id)

            When("여러 문제 정보를 조회하면") {
                val results = questionQueryAPI.getQuestionInformation(questionIds)

                Then("모든 문제 정보가 반환된다") {
                    results shouldHaveSize 2

                    val result1 = results.find { it.id == question1.id }
                    result1 shouldNotBe null
                    result1!!.creatorId shouldBe creatorId1
                    result1.questionLevel shouldBe "LEVEL1"

                    val result2 = results.find { it.id == question2.id }
                    result2 shouldNotBe null
                    result2!!.creatorId shouldBe creatorId2
                    result2.questionLevel shouldBe "LEVEL5"
                }
            }
        }

        Given("이용 가능한 문제가 존재할 때") {
            val question = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            When("문제 이용 가능 여부를 확인하면") {
                val isAvailable = questionQueryAPI.isAvailable(question.id)

                Then("이용 가능한 상태가 반환된다") {
                    isAvailable shouldBe true
                }
            }
        }

        Given("사용자가 문제를 소유하고 있을 때") {
            val question = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            saveUserQuestion(userId, question.id)

            When("사용자 문제 소유 여부를 확인하면") {
                val isOwned = questionQueryAPI.isOwned(userId, question.id)

                Then("소유 상태가 반환된다") {
                    isOwned shouldBe true
                }
            }
        }

        Given("사용자가 문제를 소유하지 않았을 때") {
            val question = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            When("사용자 문제 소유 여부를 확인하면") {
                val isOwned = questionQueryAPI.isOwned(userId, question.id)

                Then("미소유 상태가 반환된다") {
                    isOwned shouldBe false
                }
            }
        }

        Given("사용자가 여러 문제를 모두 소유하고 있을 때") {
            val question1 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
            val question2 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            saveUserQuestions(userId, listOf(question1.id, question2.id))

            val questionIds = listOf(question1.id, question2.id)

            When("여러 문제 소유 여부를 확인하면") {
                val isOwned = questionQueryAPI.isOwned(userId, questionIds)

                Then("모두 소유한 상태가 반환된다") {
                    isOwned shouldBe true
                }
            }
        }

        Given("크리에이터의 문제가 존재할 때") {
            val question1 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
            val question2 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL4,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )
            val question3 = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId2,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            val pagingInformation = PagingInformation(0, 10)

            When("크리에이터의 문제 목록을 조회하면") {
                val results = questionQueryAPI.getCreatorQuestions(creatorId1, pagingInformation)

                Then("해당 크리에이터의 문제만 반환된다") {
                    results shouldHaveSize 2
                    results.forEach { result ->
                        result.creatorId shouldBe creatorId1
                    }

                    val questionIds = results.map { it.id }
                    questionIds shouldContainExactlyInAnyOrder listOf(question1.id, question2.id)
                }
            }
        }

        Given("크리에이터가 여러 문제를 등록했을 때") {
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
                    creatorId1,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                ),
                QuestionFixtureHelper.createQuestion(
                    creatorId2,
                    questionRepository = questionRepository,
                    questionMetadataRepository = questionMetadataRepository
                )
            )

            When("크리에이터의 문제 개수를 조회하면") {
                val count = questionQueryAPI.countByCreatorId(creatorId1)

                Then("해당 크리에이터의 문제 개수가 반환된다") {
                    count shouldBe 3
                }
            }
        }

        Given("문제 콘텐츠가 존재할 때") {
            val question = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId1,
                questionLevel = QuestionLevel.LEVEL3,
                questionType = QuestionType.SelfMade,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            When("문제 콘텐츠 상세 정보를 조회하면") {
                val result = questionQueryAPI.getQuestionContent(question.id, creatorId1)

                Then("문제 콘텐츠 상세 정보가 반환된다") {
                    result.title shouldBe question.questionContent.title
                    result.description shouldBe question.questionContent.description
                    result.questionLevel shouldBe "LEVEL3"
                    result.price shouldBe 1000
                    result.questionCategoryId shouldBe question.questionContent.questionCategoryId
                }
            }
        }
    }

    private fun saveUserQuestion(userId: Long, questionId: Long) {
        val userQuestion = UserQuestion.create(userId, questionId)
        userQuestionRepository.saveAll(listOf(userQuestion))
    }

    private fun saveUserQuestions(userId: Long, questionIds: List<Long>) {
        val userQuestions = UserQuestion.create(userId, questionIds)
        userQuestionRepository.saveAll(userQuestions)
    }
}