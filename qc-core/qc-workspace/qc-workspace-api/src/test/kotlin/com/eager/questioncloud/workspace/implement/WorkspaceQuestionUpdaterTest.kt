package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
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
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionCommandAPI: QuestionCommandAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터가 기존 문제 수정") {
            val creatorId = 1L
            val questionId = 1L
            
            val command = ModifyQuestionCommand(
                creatorId = creatorId,
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
            
            justRun { questionCommandAPI.modify(any()) }
            
            When("문제를 수정하면") {
                workspaceQuestionUpdater.modifyQuestion(command)
                
                Then("QuestionCommandAPI가 호출된다") {
                    verify(exactly = 1) {
                        questionCommandAPI.modify(any())
                    }
                }
            }
        }
    }
}