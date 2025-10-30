package com.eager.questioncloud.question.api.internal

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionStatus
import com.eager.questioncloud.question.enums.Subject
import com.eager.questioncloud.question.fixture.QuestionFixtureHelper
import com.eager.questioncloud.question.repository.QuestionMetadataRepository
import com.eager.questioncloud.question.repository.QuestionRepository
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class QuestionCommandAPIImplTest(
    private val questionCommandAPI: QuestionCommandAPIImpl,
    private val questionRepository: QuestionRepository,
    private val questionMetadataRepository: QuestionMetadataRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {

    private val creatorId = 101L

    init {
        afterTest {
            dbCleaner.cleanUp()
        }

        Given("새로운 문제 등록 요청이 주어졌을 때") {
            val registerCommand = RegisterQuestionAPIRequest(
                creatorId = creatorId,
                questionCategoryId = 1L,
                subject = "Mathematics",
                title = "수학 문제 제목",
                description = "수학 문제 설명",
                thumbnail = "thumbnail.jpg",
                fileUrl = "question.pdf",
                explanationUrl = "explanation.pdf",
                questionLevel = "LEVEL3",
                price = 2000
            )

            When("문제를 등록하면") {
                val newQuestionId = questionCommandAPI.register(registerCommand)

                Then("새로운 문제가 생성되고 메타데이터가 초기화된다") {
                    val createdQuestion = questionRepository.get(newQuestionId)

                    createdQuestion.creatorId shouldBe creatorId
                    createdQuestion.questionContent.title shouldBe "수학 문제 제목"
                    createdQuestion.questionContent.description shouldBe "수학 문제 설명"
                    createdQuestion.questionContent.subject shouldBe Subject.Mathematics
                    createdQuestion.questionContent.questionLevel shouldBe QuestionLevel.LEVEL3
                    createdQuestion.questionContent.price shouldBe 2000
                    createdQuestion.questionStatus shouldBe QuestionStatus.Available

                    val questionMetadata = questionMetadataRepository.getForUpdate(createdQuestion.id)
                    questionMetadata.questionId shouldBe createdQuestion.id
                    questionMetadata.sales shouldBe 0
                    questionMetadata.reviewCount shouldBe 0
                    questionMetadata.reviewAverageRate shouldBe 0.0
                }
            }
        }

        Given("기존 문제가 존재할 때") {
            val existingQuestion = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId,
                questionLevel = QuestionLevel.LEVEL1,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            val modifyCommand = ModifyQuestionAPIRequest(
                creatorId = creatorId,
                questionId = 1L,
                questionCategoryId = 2L,
                subject = "Physics",
                title = "수정된 물리 문제",
                description = "수정된 물리 문제 설명",
                thumbnail = "modified_thumbnail.jpg",
                fileUrl = "modified_question.pdf",
                explanationUrl = "modified_explanation.pdf",
                questionLevel = "LEVEL5",
                price = 3000
            )

            When("문제를 수정하면") {
                questionCommandAPI.modify(modifyCommand)

                Then("문제 정보가 업데이트된다") {
                    val modifiedQuestion = questionRepository.get(existingQuestion.id)
                    modifiedQuestion.id shouldBe existingQuestion.id
                    modifiedQuestion.creatorId shouldBe creatorId // 변경되지 않음
                    modifiedQuestion.questionContent.questionCategoryId shouldBe 2L
                    modifiedQuestion.questionContent.subject shouldBe Subject.Physics
                    modifiedQuestion.questionContent.title shouldBe "수정된 물리 문제"
                    modifiedQuestion.questionContent.description shouldBe "수정된 물리 문제 설명"
                    modifiedQuestion.questionContent.thumbnail shouldBe "modified_thumbnail.jpg"
                    modifiedQuestion.questionContent.fileUrl shouldBe "modified_question.pdf"
                    modifiedQuestion.questionContent.explanationUrl shouldBe "modified_explanation.pdf"
                    modifiedQuestion.questionContent.questionLevel shouldBe QuestionLevel.LEVEL5
                    modifiedQuestion.questionContent.price shouldBe 3000
                }
            }
        }

        Given("삭제할 문제가 존재할 때") {
            val existingQuestion = QuestionFixtureHelper.createQuestion(
                creatorId = creatorId,
                questionRepository = questionRepository,
                questionMetadataRepository = questionMetadataRepository
            )

            existingQuestion.questionStatus shouldBe QuestionStatus.Available

            When("문제를 삭제하면") {
                questionCommandAPI.delete(existingQuestion.id, creatorId)

                Then("문제 조회 시 UNAVAILABLE_QUESTION 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        questionRepository.get(existingQuestion.id)
                    }
                    exception.error shouldBe Error.UNAVAILABLE_QUESTION
                }
            }
        }
    }
}
