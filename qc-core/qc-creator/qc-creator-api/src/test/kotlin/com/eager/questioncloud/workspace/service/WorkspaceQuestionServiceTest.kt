package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.*
import com.eager.questioncloud.scenario.CreatorScenario
import com.eager.questioncloud.utils.DBCleaner
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.exactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import org.mockito.kotlin.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class WorkspaceQuestionServiceTest(
    private val workspaceQuestionService: WorkspaceQuestionService,
    private val creatorRepository: CreatorRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockkBean
    private lateinit var questionCommandAPI: QuestionCommandAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터가 본인의 문제 목록을 조회할 때") {
            val creator = CreatorScenario.create(1).creators[0]
            creatorRepository.save(creator)
            val pagingInformation = PagingInformation.max
            val expectedQuestions = (1..10).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, it)
                    .set(QuestionInformationQueryResult::creatorId, creator.id)
                    .sample()
            }
            every { questionQueryAPI.getCreatorQuestions(creator.id, pagingInformation) } returns expectedQuestions
            
            When("조회 요청을 하면") {
                val actualQuestions = workspaceQuestionService.getMyQuestions(creator.userId, pagingInformation)
                
                Then("본인이 만든 문제 목록이 반환된다") {
                    actualQuestions shouldBe expectedQuestions
                }
            }
        }
        
        Given("크리에이터가 본인의 문제 개수를 조회할 때") {
            val creator = CreatorScenario.create(1).creators[0]
            creatorRepository.save(creator)
            val expectedQuestions = (1..10).map {
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, it)
                    .set(QuestionInformationQueryResult::creatorId, creator.id)
                    .sample()
            }
            every { questionQueryAPI.countByCreatorId(creator.id) } returns expectedQuestions.size
            
            When("개수 조회 요청을 하면") {
                val actualCount = workspaceQuestionService.countMyQuestions(creator.id)
                
                Then("본인이 만든 문제 개수가 반환된다") {
                    actualCount shouldBe expectedQuestions.size
                }
            }
        }
        
        Given("크리에이터가 본인 문제의 상세 정보를 조회할 때") {
            val creator = CreatorScenario.create(1).creators[0]
            creatorRepository.save(creator)
            val questionId = 1L
            val expectedQuestion = Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionContentQueryResult>()
                .sample()
            
            every { questionQueryAPI.getQuestionContent(any(), any()) } returns expectedQuestion
            
            When("특정 문제의 상세 조회 요청을 하면") {
                val actualContent = workspaceQuestionService.getMyQuestionContent(creator.userId, questionId)
                
                Then("해당 문제의 상세 정보가 반환된다") {
                    actualContent.title shouldBe expectedQuestion.title
                    actualContent.subject shouldBe expectedQuestion.subject
                    actualContent.questionCategoryId shouldBe expectedQuestion.questionCategoryId
                    actualContent.description shouldBe expectedQuestion.description
                    actualContent.thumbnail shouldBe expectedQuestion.thumbnail
                    actualContent.fileUrl shouldBe expectedQuestion.fileUrl
                    actualContent.explanationUrl shouldBe expectedQuestion.explanationUrl
                    actualContent.questionLevel shouldBe expectedQuestion.questionLevel
                    actualContent.price shouldBe expectedQuestion.price
                }
            }
        }
        
        Given("크리에이터가 새로운 문제를 등록할 때") {
            val creator = CreatorScenario.create(1).creators[0]
            creatorRepository.save(creator)
            val command = Fixture.fixtureMonkey.giveMeKotlinBuilder<RegisterQuestionCommand>().sample()
            every { questionCommandAPI.register(any(), any()) } returns 1L
            
            When("문제 등록 요청을 하면") {
                workspaceQuestionService.registerQuestion(creator.userId, command)
                
                Then("문제가 성공적으로 등록된다") {
                    verify(exactly(1)) { questionCommandAPI.register(creator.id, command) }
                }
            }
        }
        
        Given("크리에이터가 기존 문제를 수정할 때") {
            val creator = CreatorScenario.create(1).creators[0]
            creatorRepository.save(creator)
            val command = Fixture.fixtureMonkey.giveMeKotlinBuilder<ModifyQuestionCommand>().sample()
            val questionId = 1L
            
            justRun { questionCommandAPI.modify(any(), any()) }
            
            When("문제 수정 요청을 하면") {
                workspaceQuestionService.modifyQuestion(creator.userId, questionId, command)
                
                Then("문제가 성공적으로 수정된다") {
                    verify(exactly(1)) { questionCommandAPI.modify(questionId, command) }
                }
            }
        }
        
        Given("크리에이터가 본인 문제를 삭제할 때") {
            val creator = CreatorScenario.create(1).creators[0]
            creatorRepository.save(creator)
            val questionId = 1L
            
            justRun { questionCommandAPI.delete(any(), any()) }
            
            When("문제 삭제 요청을 하면") {
                workspaceQuestionService.deleteQuestion(creator.userId, questionId)
                
                Then("문제가 성공적으로 삭제된다") {
                    verify(exactly(1)) { questionCommandAPI.delete(questionId, creator.id) }
                }
            }
        }
    }
}