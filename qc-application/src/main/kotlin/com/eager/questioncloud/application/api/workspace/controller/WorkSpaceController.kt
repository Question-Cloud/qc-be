package com.eager.questioncloud.application.api.workspace.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.workspace.dto.*
import com.eager.questioncloud.application.api.workspace.service.WorkspacePostService
import com.eager.questioncloud.application.api.workspace.service.WorkspaceProfileService
import com.eager.questioncloud.application.api.workspace.service.WorkspaceQuestionService
import com.eager.questioncloud.application.api.workspace.service.WorkspaceRegisterService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostPreview
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    @PreAuthorize("hasAnyRole('ROLE_NormalUser')")
    fun registerCreator(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestBody request: @Valid RegisterCreatorRequest
    ): RegisterCreatorResponse {
        val creator = workspaceRegisterService.register(userPrincipal.user, request.mainSubject, request.introduction)
        return RegisterCreatorResponse(creator.id)
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    fun getMyCreatorInformation(@AuthenticationPrincipal userPrincipal: UserPrincipal): CreatorProfileResponse {
        val me = userPrincipal.creator!!
        return CreatorProfileResponse(me.mainSubject, me.introduction)
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    fun updateMyCreatorInformation(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid UpdateCreatorProfileRequest
    ): DefaultResponse {
        workspaceProfileService.updateCreatorProfile(userPrincipal.creator!!, request.mainSubject, request.introduction)
        return success()
    }

    @GetMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    fun getQuestions(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<QuestionInformation> {
        val total = workspaceQuestionService.countMyQuestions(userPrincipal.creator!!.id)
        val questions = workspaceQuestionService.getMyQuestions(
            userPrincipal.creator.id, pagingInformation
        )
        return PagingResponse(total, questions)
    }

    @GetMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    fun getQuestion(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable questionId: Long
    ): QuestionContentResponse {
        val questionContent = workspaceQuestionService.getMyQuestionContent(userPrincipal.creator!!.id, questionId)
        return QuestionContentResponse(questionContent)
    }

    @PostMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    fun register(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterQuestionRequest
    ): DefaultResponse {
        workspaceQuestionService.registerQuestion(userPrincipal.creator!!.id, request.toQuestionContent())
        return success()
    }

    @PatchMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    fun modify(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable questionId: Long,
        @RequestBody request: @Valid ModifyQuestionRequest
    ): DefaultResponse {
        workspaceQuestionService.modifyQuestion(userPrincipal.creator!!.id, questionId, request.toQuestionContent())
        return success()
    }

    @DeleteMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    fun delete(@AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable questionId: Long): DefaultResponse {
        workspaceQuestionService.deleteQuestion(userPrincipal.creator!!.id, questionId)
        return success()
    }

    @GetMapping("/board")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    fun creatorQuestionBoardList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<PostPreview> {
        val total = workspacePostService.countCreatorPost(userPrincipal.creator!!.id)
        val boards = workspacePostService.getCreatorPosts(
            userPrincipal.creator.id,
            pagingInformation
        )
        return PagingResponse(total, boards)
    }
}
