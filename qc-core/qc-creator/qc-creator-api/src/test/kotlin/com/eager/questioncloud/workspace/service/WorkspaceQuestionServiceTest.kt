package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.*
import com.eager.questioncloud.utils.DBCleaner
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
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
) : FunSpec() {
    @MockkBean
    private lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockkBean
    private lateinit var questionCommandAPI: QuestionCommandAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        test("만든 문제 목록을 조회할 수 있다") {
            val userId = 1L
            val creatorId = 1L
            val pagingInformation = PagingInformation.max
            val creator = creatorRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                    .set(Creator::id, creatorId)
                    .set(Creator::userId, userId)
                    .sample()
            )
            val expectedQuestions = listOf(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionInformationQueryResult>()
                    .set(QuestionInformationQueryResult::id, creatorId)
                    .sample()
            )
            every { questionQueryAPI.getCreatorQuestions(creator.id, pagingInformation) } returns expectedQuestions
            
            val actualQuestions = workspaceQuestionService.getMyQuestions(userId, pagingInformation)
            
            actualQuestions shouldBe expectedQuestions
        }
        
        test("만든 문제 목록의 개수를 조회할 수 있다") {
            val userId = 1L
            val creatorId = 1L
            val creator = creatorRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                    .set(Creator::id, creatorId)
                    .set(Creator::userId, userId)
                    .sample()
            )
            val expectedCount = 5
            every { questionQueryAPI.countByCreatorId(creator.id) } returns expectedCount
            
            val actualCount = workspaceQuestionService.countMyQuestions(userId)
            
            actualCount shouldBe expectedCount
        }
        
        test("만든 문제의 상세 정보를 조회할 수 있다") {
            val userId = 1L
            val questionId = 1L
            val creatorId = 1L
            val creator = creatorRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                    .set(Creator::id, creatorId)
                    .set(Creator::userId, userId)
                    .sample()
            )
            val questionContentQueryResult =
                Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionContentQueryResult>().sample()
            every { questionQueryAPI.getQuestionContent(questionId, creator.id) } returns questionContentQueryResult
            
            val actualContent = workspaceQuestionService.getMyQuestionContent(userId, questionId)
            
            actualContent.title shouldBe questionContentQueryResult.title
            actualContent.subject shouldBe questionContentQueryResult.subject
            actualContent.questionCategoryId shouldBe questionContentQueryResult.questionCategoryId
            actualContent.description shouldBe questionContentQueryResult.description
            actualContent.thumbnail shouldBe questionContentQueryResult.thumbnail
            actualContent.fileUrl shouldBe questionContentQueryResult.fileUrl
            actualContent.explanationUrl shouldBe questionContentQueryResult.explanationUrl
            actualContent.questionLevel shouldBe questionContentQueryResult.questionLevel
            actualContent.price shouldBe questionContentQueryResult.price
        }
        
        test("문제를 등록할 수 있다") {
            val userId = 1L
            val creatorId = 1L
            val creator = creatorRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                    .set(Creator::id, creatorId)
                    .set(Creator::userId, userId)
                    .sample()
            )
            val command = Fixture.fixtureMonkey.giveMeKotlinBuilder<RegisterQuestionCommand>().sample()
            every { questionCommandAPI.register(any(), any()) } returns 1L
            
            workspaceQuestionService.registerQuestion(userId, command)
            
            verify(exactly(1)) { questionCommandAPI.register(creator.id, command) }
        }
        
        test("문제를 수정할 수 있다") {
            val userId = 1L
            val questionId = 1L
            val creatorId = 1L
            creatorRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                    .set(Creator::id, creatorId)
                    .set(Creator::userId, userId)
                    .sample()
            )
            val command = Fixture.fixtureMonkey.giveMeKotlinBuilder<ModifyQuestionCommand>().sample()
            justRun { questionCommandAPI.modify(any(), any()) }
            
            workspaceQuestionService.modifyQuestion(userId, questionId, command)
            
            verify(exactly(1)) { questionCommandAPI.modify(questionId, command) }
        }
        
        test("문제를 삭제할 수 있다") {
            val userId = 1L
            val questionId = 1L
            val creatorId = 1L
            val creator = creatorRepository.save(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                    .set(Creator::id, creatorId)
                    .set(Creator::userId, userId)
                    .sample()
            )
            justRun { questionCommandAPI.delete(any(), any()) }
            
            workspaceQuestionService.deleteQuestion(userId, questionId)
            
            verify(exactly(1)) { questionCommandAPI.delete(questionId, creator.id) }
        }
    }
}