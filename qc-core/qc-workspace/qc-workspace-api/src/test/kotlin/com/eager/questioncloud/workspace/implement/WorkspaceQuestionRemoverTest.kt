package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.test.utils.DBCleaner
import com.eager.questioncloud.workspace.command.DeleteQuestionCommand
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
class WorkspaceQuestionRemoverTest(
    private val workspaceQuestionRemover: WorkspaceQuestionRemover,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionCommandAPI: QuestionCommandAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터가 본인 문제 삭제") {
            val creatorId = 1L
            val questionId = 1L
            
            val command = DeleteQuestionCommand(
                creatorId = creatorId,
                questionId = questionId
            )
            
            justRun { questionCommandAPI.delete(questionId, creatorId) }
            
            When("문제를 삭제하면") {
                workspaceQuestionRemover.deleteQuestion(command)
                
                Then("QuestionCommandAPI가 호출된다") {
                    verify(exactly = 1) {
                        questionCommandAPI.delete(questionId, creatorId)
                    }
                }
            }
        }
    }
}