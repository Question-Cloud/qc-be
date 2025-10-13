package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.scenario.CreatorScenario
import com.eager.questioncloud.utils.DBCleaner
import com.eager.questioncloud.workspace.command.ModifyQuestionCommand
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.justRun
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class WorkspaceQuestionUpdaterTest(
    private val workspaceQuestionUpdater: WorkspaceQuestionUpdater,
    private val creatorRepository: CreatorRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionCommandAPI: QuestionCommandAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터가 기존 문제 수정") {
            val creatorScenario = CreatorScenario.create(1)
            val creator = creatorRepository.save(creatorScenario.creators[0])
            val userId = creator.userId
            val creatorId = creator.id
            val questionId = 1L
            
            val command = ModifyQuestionCommand(
                userId = userId,
                questionId = questionId,
                questionCategoryId = 1L,
                subject = "수학",
                title = "수정된 문제",
                description = "수정된 설명",
                thumbnail = "thumb_updated.jpg",
                fileUrl = "file_updated.pdf",
                explanationUrl = "exp_updated.pdf",
                questionLevel = "고급",
                price = 15000
            )
            
            justRun { questionCommandAPI.modify(creatorId, any()) }
            
            When("문제를 수정하면") {
                workspaceQuestionUpdater.modifyQuestion(command)
                
                Then("QuestionCommandAPI가 호출된다") {
                    verify(exactly = 1) {
                        questionCommandAPI.modify(creatorId, any())
                    }
                }
            }
        }
    }
}