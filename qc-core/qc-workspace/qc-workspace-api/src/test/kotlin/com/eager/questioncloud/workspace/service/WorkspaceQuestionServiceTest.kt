package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.workspace.command.DeleteQuestionCommand
import com.eager.questioncloud.workspace.command.ModifyQuestionCommand
import com.eager.questioncloud.workspace.command.RegisterQuestionCommand
import com.eager.questioncloud.workspace.dto.CreatorQuestionInformation
import com.eager.questioncloud.workspace.dto.MyQuestionContent
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionReader
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionRegister
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionRemover
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionUpdater
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class WorkspaceQuestionServiceTest : BehaviorSpec() {
    private val workspaceQuestionReader = mockk<WorkspaceQuestionReader>()
    private val workspaceQuestionRegister = mockk<WorkspaceQuestionRegister>()
    private val workspaceQuestionUpdater = mockk<WorkspaceQuestionUpdater>()
    private val workspaceQuestionRemover = mockk<WorkspaceQuestionRemover>()
    
    private val workspaceQuestionService = WorkspaceQuestionService(
        workspaceQuestionReader,
        workspaceQuestionRegister,
        workspaceQuestionUpdater,
        workspaceQuestionRemover
    )
    
    init {
        afterEach {
            clearMocks(workspaceQuestionReader, workspaceQuestionReader, workspaceQuestionUpdater, workspaceQuestionRemover)
        }
        
        Given("크리에이터가 본인의 문제 목록 조회") {
            val creatorId = 1L
            val pagingInformation = PagingInformation(0, 10)
            
            val expectedQuestions = listOf(
                CreatorQuestionInformation(
                    id = 1L,
                    creatorId = creatorId,
                    title = "문제 1",
                    subject = "수학",
                    parentCategory = "수학",
                    childCategory = "대수",
                    thumbnail = "thumb1.jpg",
                    questionLevel = "중급",
                    price = 10000,
                    rate = 4.5
                ),
                CreatorQuestionInformation(
                    id = 2L,
                    creatorId = creatorId,
                    title = "문제 2",
                    subject = "수학",
                    parentCategory = "수학",
                    childCategory = "기하",
                    thumbnail = "thumb2.jpg",
                    questionLevel = "고급",
                    price = 15000,
                    rate = 4.8
                )
            )
            
            every { workspaceQuestionReader.getMyQuestions(creatorId, pagingInformation) } returns expectedQuestions
            
            When("문제 목록을 조회하면") {
                val result = workspaceQuestionService.getMyQuestions(creatorId, pagingInformation)
                
                Then("문제 목록이 반환된다") {
                    result shouldBe expectedQuestions
                    
                    verify(exactly = 1) { workspaceQuestionReader.getMyQuestions(creatorId, pagingInformation) }
                }
            }
        }
        
        Given("존재하지 않는 크리에이터의 문제 목록 조회") {
            val creatorId = 1L
            val pagingInformation = PagingInformation(0, 10)
            
            every {
                workspaceQuestionReader.getMyQuestions(
                    creatorId,
                    pagingInformation
                )
            } throws CoreException(com.eager.questioncloud.common.exception.Error.NOT_FOUND)
            
            When("문제 목록을 조회하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        workspaceQuestionService.getMyQuestions(creatorId, pagingInformation)
                    }
                    
                    verify(exactly = 1) { workspaceQuestionReader.getMyQuestions(creatorId, pagingInformation) }
                }
            }
        }
        
        Given("크리에이터가 본인의 문제 개수 조회") {
            val creatorId = 1L
            
            val expectedCount = 10
            
            every { workspaceQuestionReader.countMyQuestions(creatorId) } returns expectedCount
            
            When("문제 개수를 조회하면") {
                val result = workspaceQuestionService.countMyQuestions(creatorId)
                
                Then("문제 개수가 반환된다") {
                    result shouldBe expectedCount
                    
                    verify(exactly = 1) { workspaceQuestionReader.countMyQuestions(creatorId) }
                }
            }
        }
        
        Given("크리에이터가 본인 문제의 상세 정보 조회") {
            val creatorId = 1L
            val questionId = 1L
            
            val expectedContent = MyQuestionContent(
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
            
            every { workspaceQuestionReader.getMyQuestionContent(creatorId, questionId) } returns expectedContent
            
            When("문제 상세 정보를 조회하면") {
                val result = workspaceQuestionService.getMyQuestionContent(creatorId, questionId)
                
                Then("문제 상세 정보가 반환된다") {
                    result shouldBe expectedContent
                    
                    verify(exactly = 1) { workspaceQuestionReader.getMyQuestionContent(creatorId, questionId) }
                }
            }
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
            
            justRun { workspaceQuestionRegister.registerQuestion(command) }
            
            When("문제를 등록하면") {
                workspaceQuestionService.registerQuestion(command)
                
                Then("문제가 등록된다") {
                    verify(exactly = 1) { workspaceQuestionRegister.registerQuestion(command) }
                }
            }
        }
        
        Given("크리에이터가 기존 문제 수정") {
            val creatorId = 1L
            val questionId = 1L
            val command = ModifyQuestionCommand(
                creatorId,
                questionId,
                questionCategoryId = 1L,
                subject = "수학",
                title = "수정된 문제",
                description = "수정된 설명",
                thumbnail = "thumb.jpg",
                fileUrl = "file.pdf",
                explanationUrl = "exp.pdf",
                questionLevel = "고급",
                price = 15000
            )
            
            justRun { workspaceQuestionUpdater.modifyQuestion(any()) }
            
            When("문제를 수정하면") {
                workspaceQuestionService.modifyQuestion(command)
                
                Then("문제가 수정된다") {
                    verify(exactly = 1) { workspaceQuestionUpdater.modifyQuestion(any()) }
                }
            }
        }
        
        Given("크리에이터가 본인 문제 삭제") {
            val creatorId = 1L
            val questionId = 1L
            val deleteQuestionCommand = DeleteQuestionCommand(creatorId, questionId)
            
            justRun { workspaceQuestionRemover.deleteQuestion(deleteQuestionCommand) }
            
            When("문제를 삭제하면") {
                workspaceQuestionService.deleteQuestion(deleteQuestionCommand)
                
                Then("문제가 삭제된다") {
                    verify(exactly = 1) { workspaceQuestionRemover.deleteQuestion(deleteQuestionCommand) }
                }
            }
        }
    }
}