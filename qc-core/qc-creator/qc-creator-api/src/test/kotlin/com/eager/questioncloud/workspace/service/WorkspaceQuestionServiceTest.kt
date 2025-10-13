package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.ModifyQuestionCommand
import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.question.api.internal.RegisterQuestionCommand
import com.eager.questioncloud.workspace.dto.CreatorQuestionInformation
import com.eager.questioncloud.workspace.dto.MyQuestionContent
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionReader
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class WorkspaceQuestionServiceTest : BehaviorSpec() {
    private val creatorRepository = mockk<CreatorRepository>()
    private val workspaceQuestionReader = mockk<WorkspaceQuestionReader>()
    private val questionCommandAPI = mockk<QuestionCommandAPI>()
    
    private val workspaceQuestionService = WorkspaceQuestionService(
        workspaceQuestionReader,
        creatorRepository,
        questionCommandAPI
    )
    
    init {
        afterEach {
            clearMocks(creatorRepository, workspaceQuestionReader, questionCommandAPI)
        }
        
        Given("크리에이터가 본인의 문제 목록 조회") {
            val userId = 1L
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
            
            every { workspaceQuestionReader.getMyQuestions(userId, pagingInformation) } returns expectedQuestions
            
            When("문제 목록을 조회하면") {
                val result = workspaceQuestionService.getMyQuestions(userId, pagingInformation)
                
                Then("문제 목록이 반환된다") {
                    result shouldBe expectedQuestions
                    
                    verify(exactly = 1) { workspaceQuestionReader.getMyQuestions(userId, pagingInformation) }
                }
            }
        }
        
        Given("존재하지 않는 크리에이터의 문제 목록 조회") {
            val userId = 1L
            val pagingInformation = PagingInformation(0, 10)
            
            every {
                workspaceQuestionReader.getMyQuestions(
                    userId,
                    pagingInformation
                )
            } throws CoreException(com.eager.questioncloud.common.exception.Error.NOT_FOUND)
            
            When("문제 목록을 조회하려고 하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        workspaceQuestionService.getMyQuestions(userId, pagingInformation)
                    }
                    
                    verify(exactly = 1) { workspaceQuestionReader.getMyQuestions(userId, pagingInformation) }
                }
            }
        }
        
        Given("크리에이터가 본인의 문제 개수 조회") {
            val userId = 1L
            
            val expectedCount = 10
            
            every { workspaceQuestionReader.countMyQuestions(userId) } returns expectedCount
            
            When("문제 개수를 조회하면") {
                val result = workspaceQuestionService.countMyQuestions(userId)
                
                Then("문제 개수가 반환된다") {
                    result shouldBe expectedCount
                    
                    verify(exactly = 1) { workspaceQuestionReader.countMyQuestions(userId) }
                }
            }
        }
        
        Given("크리에이터가 본인 문제의 상세 정보 조회") {
            val userId = 1L
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
            
            every { workspaceQuestionReader.getMyQuestionContent(userId, questionId) } returns expectedContent
            
            When("문제 상세 정보를 조회하면") {
                val result = workspaceQuestionService.getMyQuestionContent(userId, questionId)
                
                Then("문제 상세 정보가 반환된다") {
                    result shouldBe expectedContent
                    
                    verify(exactly = 1) { workspaceQuestionReader.getMyQuestionContent(userId, questionId) }
                }
            }
        }
        
        Given("크리에이터가 새로운 문제 등록") {
            val userId = 1L
            
            val creator = Creator.create(userId, "수학", "수학 전문")
            val command = RegisterQuestionCommand(
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
            
            every { creatorRepository.findByUserId(userId) } returns creator
            every { questionCommandAPI.register(any(), command) } returns 1L
            
            When("문제를 등록하면") {
                workspaceQuestionService.registerQuestion(userId, command)
                
                Then("문제가 등록된다") {
                    verify(exactly = 1) { creatorRepository.findByUserId(userId) }
                    verify(exactly = 1) { questionCommandAPI.register(any(), command) }
                }
            }
        }
        
        Given("크리에이터가 기존 문제 수정") {
            val userId = 1L
            val questionId = 1L
            
            val creator = Creator.create(userId, "수학", "수학 전문")
            val command = ModifyQuestionCommand(
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
            
            every { creatorRepository.findByUserId(userId) } returns creator
            justRun { questionCommandAPI.modify(any(), command) }
            
            When("문제를 수정하면") {
                workspaceQuestionService.modifyQuestion(userId, questionId, command)
                
                Then("문제가 수정된다") {
                    verify(exactly = 1) { creatorRepository.findByUserId(userId) }
                    verify(exactly = 1) { questionCommandAPI.modify(any(), command) }
                }
            }
        }
        
        Given("크리에이터가 본인 문제 삭제") {
            val userId = 1L
            val questionId = 1L
            
            val creator = Creator.create(userId, "수학", "수학 전문")
            
            every { creatorRepository.findByUserId(userId) } returns creator
            justRun { questionCommandAPI.delete(questionId, any()) }
            
            When("문제를 삭제하면") {
                workspaceQuestionService.deleteQuestion(userId, questionId)
                
                Then("문제가 삭제된다") {
                    verify(exactly = 1) { creatorRepository.findByUserId(userId) }
                    verify(exactly = 1) { questionCommandAPI.delete(questionId, any()) }
                }
            }
        }
    }
}