package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.QuestionContentQueryResult
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.scenario.CreatorScenario
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class WorkspaceQuestionReaderTest(
    private val workspaceQuestionReader: WorkspaceQuestionReader,
    private val creatorRepository: CreatorRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터가 본인의 문제 목록 조회") {
            val creatorScenario = CreatorScenario.create(1)
            val creator = creatorRepository.save(creatorScenario.creators[0])
            val userId = creator.userId
            val creatorId = creator.id
            val pagingInformation = PagingInformation(0, 10)
            
            val expectedQuestions = listOf(
                QuestionInformationQueryResult(
                    id = 1L,
                    creatorId = creatorId,
                    title = "문제 1",
                    description = "어려운 문제임",
                    subject = "수학",
                    parentCategory = "수학",
                    childCategory = "대수",
                    thumbnail = "thumb1.jpg",
                    questionLevel = "중급",
                    price = 10000,
                    rate = 4.5
                ),
                QuestionInformationQueryResult(
                    id = 2L,
                    creatorId = creatorId,
                    title = "문제 2",
                    description = "어려운 문제임",
                    subject = "수학",
                    parentCategory = "수학",
                    childCategory = "기하",
                    thumbnail = "thumb2.jpg",
                    questionLevel = "고급",
                    price = 15000,
                    rate = 4.8
                )
            )
            
            every { questionQueryAPI.getCreatorQuestions(creatorId, pagingInformation) } returns expectedQuestions
            
            When("크리에이터 본인 문제 목록을 조회하면") {
                val result = workspaceQuestionReader.getMyQuestions(userId, pagingInformation)
                
                Then("문제 목록이 반환된다") {
                    result.size shouldBe expectedQuestions.size
                    
                    result[0].id shouldBe expectedQuestions[0].id
                    result[0].title shouldBe expectedQuestions[0].title
                    result[0].subject shouldBe expectedQuestions[0].subject
                    result[0].parentCategory shouldBe expectedQuestions[0].parentCategory
                    result[0].childCategory shouldBe expectedQuestions[0].childCategory
                    result[0].thumbnail shouldBe expectedQuestions[0].thumbnail
                    result[0].questionLevel shouldBe expectedQuestions[0].questionLevel
                    result[0].price shouldBe expectedQuestions[0].price
                    result[0].rate shouldBe expectedQuestions[0].rate
                    
                    result[1].id shouldBe expectedQuestions[1].id
                    result[1].title shouldBe expectedQuestions[1].title
                    result[1].subject shouldBe expectedQuestions[1].subject
                    result[1].parentCategory shouldBe expectedQuestions[1].parentCategory
                    result[1].childCategory shouldBe expectedQuestions[1].childCategory
                    result[1].thumbnail shouldBe expectedQuestions[1].thumbnail
                    result[1].questionLevel shouldBe expectedQuestions[1].questionLevel
                    result[1].price shouldBe expectedQuestions[1].price
                    result[1].rate shouldBe expectedQuestions[1].rate
                }
            }
        }
        
        Given("존재하지 않는 크리에이터의 문제 목록 조회") {
            val unknownUserId = 999L
            val pagingInformation = PagingInformation(0, 10)
            
            When("문제 목록을 조회하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        workspaceQuestionReader.getMyQuestions(unknownUserId, pagingInformation)
                    }
                }
            }
        }
        
        Given("크리에이터가 본인의 문제 개수 조회") {
            val creatorScenario = CreatorScenario.create(1)
            val creator = creatorRepository.save(creatorScenario.creators[0])
            val userId = creator.userId
            val creatorId = creator.id
            val expectedCount = 10
            
            every { questionQueryAPI.countByCreatorId(creatorId) } returns expectedCount
            
            When("문제 개수를 조회하면") {
                val result = workspaceQuestionReader.countMyQuestions(userId)
                
                Then("문제 개수가 반환된다") {
                    result shouldBe expectedCount
                }
            }
        }
        
        Given("존재하지 않는 크리에이터의 문제 개수 조회") {
            val unknownUserId = 999L
            
            When("문제 개수를 조회하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        workspaceQuestionReader.countMyQuestions(unknownUserId)
                    }
                }
            }
        }
        
        Given("크리에이터가 본인 문제의 상세 정보 조회") {
            val creatorScenario = CreatorScenario.create(1)
            val creator = creatorRepository.save(creatorScenario.creators[0])
            val userId = creator.userId
            val creatorId = creator.id
            val questionId = 1L
            
            val expectedContent = QuestionContentQueryResult(
                questionCategoryId = 1L,
                subject = "수학",
                title = "문제 제목",
                description = "문제 설명",
                thumbnail = "thumb.jpg",
                fileUrl = "file.pdf",
                explanationUrl = "exp.pdf",
                questionLevel = "중급",
                price = 10000
            )
            
            every { questionQueryAPI.getQuestionContent(questionId, creatorId) } returns expectedContent
            
            When("문제 상세 정보를 조회하면") {
                val result = workspaceQuestionReader.getMyQuestionContent(userId, questionId)
                
                Then("문제 상세 정보가 반환된다") {
                    result.questionCategoryId shouldBe expectedContent.questionCategoryId
                    result.subject shouldBe expectedContent.subject
                    result.title shouldBe expectedContent.title
                    result.description shouldBe expectedContent.description
                    result.thumbnail shouldBe expectedContent.thumbnail
                    result.fileUrl shouldBe expectedContent.fileUrl
                    result.explanationUrl shouldBe expectedContent.explanationUrl
                    result.questionLevel shouldBe expectedContent.questionLevel
                    result.price shouldBe expectedContent.price
                }
            }
        }
        
        Given("존재하지 않는 크리에이터의 문제 상세 정보 조회") {
            val unknownUserId = 999L
            val questionId = 1L
            
            When("문제 상세 정보를 조회하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        workspaceQuestionReader.getMyQuestionContent(unknownUserId, questionId)
                    }
                }
            }
        }
    }
}