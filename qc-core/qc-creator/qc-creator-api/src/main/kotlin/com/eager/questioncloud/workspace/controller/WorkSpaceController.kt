package com.eager.questioncloud.workspace.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.workspace.command.DeleteQuestionCommand
import com.eager.questioncloud.workspace.dto.*
import com.eager.questioncloud.workspace.service.WorkspacePostService
import com.eager.questioncloud.workspace.service.WorkspaceProfileService
import com.eager.questioncloud.workspace.service.WorkspaceQuestionService
import com.eager.questioncloud.workspace.service.WorkspaceRegisterService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/workspace")
class WorkSpaceController(
    private val workspaceProfileService: WorkspaceProfileService,
    private val workspacePostService: WorkspacePostService,
    private val workspaceQuestionService: WorkspaceQuestionService,
    private val workspaceRegisterService: WorkspaceRegisterService,
) {
    @PostMapping("/register")
    fun registerCreator(
        userPrincipal: UserPrincipal, @RequestBody request: @Valid RegisterCreatorRequest
    ): RegisterCreatorResponse {
        val creator = workspaceRegisterService.register(userPrincipal.userId, request.mainSubject, request.introduction)
        return RegisterCreatorResponse(creator.id)
    }
    
    @GetMapping("/me")
    fun getMyCreatorInformation(userPrincipal: UserPrincipal): CreatorProfileResponse {
        val me = workspaceProfileService.me(userPrincipal.userId)
        return CreatorProfileResponse(me.mainSubject, me.introduction)
    }
    
    @PatchMapping("/me")
    fun updateMyCreatorInformation(
        userPrincipal: UserPrincipal,
        @RequestBody request: @Valid UpdateCreatorProfileRequest
    ): DefaultResponse {
        workspaceProfileService.updateCreatorProfile(userPrincipal.userId, request.mainSubject, request.introduction)
        return DefaultResponse.success()
    }
    
    @GetMapping("/question")
    fun getQuestions(
        userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<CreatorQuestionInformation> {
        val total = workspaceQuestionService.countMyQuestions(userPrincipal.userId)
        val questions = workspaceQuestionService.getMyQuestions(
            userPrincipal.userId, pagingInformation
        )
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
        userPrincipal: UserPrincipal,
        @PathVariable questionId: Long
    ): MyQuestionContentResponse {
        val questionContent = workspaceQuestionService.getMyQuestionContent(userPrincipal.userId, questionId)
        return MyQuestionContentResponse(questionContent)
    }
    
    @PostMapping("/question")
    fun register(
        userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterQuestionRequest
    ): DefaultResponse {
        workspaceQuestionService.registerQuestion(request.toCommand(userPrincipal.userId))
        return DefaultResponse.success()
    }
    
    @PatchMapping("/question/{questionId}")
    fun modify(
        userPrincipal: UserPrincipal, @PathVariable questionId: Long,
        @RequestBody request: @Valid ModifyQuestionRequest
    ): DefaultResponse {
        workspaceQuestionService.modifyQuestion(request.toCommand(userPrincipal.userId, questionId))
        return DefaultResponse.success()
    }
    
    @DeleteMapping("/question/{questionId}")
    fun delete(userPrincipal: UserPrincipal, @PathVariable questionId: Long): DefaultResponse {
        workspaceQuestionService.deleteQuestion(DeleteQuestionCommand(userPrincipal.userId, questionId))
        return DefaultResponse.success()
    }
    
    @GetMapping("/board")
    fun creatorQuestionBoardList(
        userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<CreatorPostItem> {
        val total = workspacePostService.countCreatorPost(userPrincipal.userId)
        val boards = workspacePostService.getCreatorPosts(
            userPrincipal.userId,
            pagingInformation
        )
        return PagingResponse(total, boards)
    }
}
