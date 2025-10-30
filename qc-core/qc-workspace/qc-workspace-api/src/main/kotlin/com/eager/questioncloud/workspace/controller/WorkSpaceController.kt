package com.eager.questioncloud.workspace.controller

import com.eager.questioncloud.common.auth.CreatorPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.workspace.command.DeleteQuestionCommand
import com.eager.questioncloud.workspace.dto.*
import com.eager.questioncloud.workspace.service.WorkspacePostService
import com.eager.questioncloud.workspace.service.WorkspaceProfileService
import com.eager.questioncloud.workspace.service.WorkspaceQuestionService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/workspace")
class WorkSpaceController(
    private val workspaceProfileService: WorkspaceProfileService,
    private val workspacePostService: WorkspacePostService,
    private val workspaceQuestionService: WorkspaceQuestionService,
) {
    @GetMapping("/me")
    fun getMyCreatorInformation(creatorPrincipal: CreatorPrincipal): CreatorProfileResponse {
        val profile = workspaceProfileService.getProfile(creatorPrincipal.creatorId)
        return CreatorProfileResponse(profile)
    }
    
    @PatchMapping("/me")
    fun updateMyCreatorInformation(
        creatorPrincipal: CreatorPrincipal,
        @RequestBody request: @Valid UpdateCreatorProfileRequest
    ): DefaultResponse {
        workspaceProfileService.updateCreatorProfile(creatorPrincipal.creatorId, request.mainSubject, request.introduction)
        return DefaultResponse.success()
    }
    
    @GetMapping("/question")
    fun getQuestions(
        creatorPrincipal: CreatorPrincipal,
        pagingInformation: PagingInformation
    ): PagingResponse<CreatorQuestionInformation> {
        val total = workspaceQuestionService.countMyQuestions(creatorPrincipal.creatorId)
        val questions = workspaceQuestionService.getMyQuestions(creatorPrincipal.creatorId, pagingInformation)
        return PagingResponse(
            total,
            questions.map {
                CreatorQuestionInformation(
                    it.id,
                    it.creatorId,
                    it.title,
                    it.subject,
                    it.parentCategory,
                    it.childCategory,
                    it.thumbnail,
                    it.questionLevel,
                    it.price,
                    it.rate
                )
            })
    }
    
    @GetMapping("/question/{questionId}")
    fun getQuestion(
        creatorPrincipal: CreatorPrincipal,
        @PathVariable questionId: Long
    ): MyQuestionContentResponse {
        val questionContent = workspaceQuestionService.getMyQuestionContent(creatorPrincipal.creatorId, questionId)
        return MyQuestionContentResponse(questionContent)
    }
    
    @PostMapping("/question")
    fun register(
        creatorPrincipal: CreatorPrincipal,
        @RequestBody request: @Valid RegisterQuestionRequest
    ): DefaultResponse {
        workspaceQuestionService.registerQuestion(request.toCommand(creatorPrincipal.creatorId))
        return DefaultResponse.success()
    }
    
    @PatchMapping("/question/{questionId}")
    fun modify(
        creatorPrincipal: CreatorPrincipal, @PathVariable questionId: Long,
        @RequestBody request: @Valid ModifyQuestionRequest
    ): DefaultResponse {
        workspaceQuestionService.modifyQuestion(request.toCommand(creatorPrincipal.creatorId, questionId))
        return DefaultResponse.success()
    }
    
    @DeleteMapping("/question/{questionId}")
    fun delete(creatorPrincipal: CreatorPrincipal, @PathVariable questionId: Long): DefaultResponse {
        workspaceQuestionService.deleteQuestion(DeleteQuestionCommand(creatorPrincipal.creatorId, questionId))
        return DefaultResponse.success()
    }
    
    @GetMapping("/board")
    fun creatorQuestionBoardList(
        creatorPrincipal: CreatorPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<CreatorPostItem> {
        val total = workspacePostService.countCreatorPost(creatorPrincipal.creatorId)
        val boards = workspacePostService.getCreatorPosts(creatorPrincipal.creatorId, pagingInformation)
        return PagingResponse(total, boards)
    }
}
