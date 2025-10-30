package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.test.utils.DBCleaner
import com.eager.questioncloud.workspace.command.RegisterQuestionCommand
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class WorkspaceQuestionRegisterTest(
    private val workspaceQuestionRegister: WorkspaceQuestionRegister,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionCommandAPI: QuestionCommandAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("크리에이터가 새로운 문제 등록") {
            val creatorId = 1L
            
            val command = RegisterQuestionCommand(
                creatorId = creatorId,
                questionCategoryId = 1L,
                subject = "수학",
                title = "새 문제",
                description = "문제 설명",
                thumbnail = "thumb.jpg",
                fileUrl = "file.pdf",
                explanationUrl = "exp.pdf",
                questionLevel = "중급",
                price = 10000
            )
            
            every {
                questionCommandAPI.register(any())
            } returns 1L
            
            When("문제를 등록하면") {
                workspaceQuestionRegister.registerQuestion(command)
                
                Then("QuestionCommandAPI가 호출된다") {
                    verify(exactly = 1) {
                        questionCommandAPI.register(any())
                    }
                }
            }
        }
    }
}