package com.eager.questioncloud.application.api.workspace.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.workspace.dto.*
import com.eager.questioncloud.application.api.creator.service.CreatorPostService
import com.eager.questioncloud.application.api.creator.service.CreatorProfileService
import com.eager.questioncloud.application.api.creator.service.CreatorQuestionService
import com.eager.questioncloud.application.api.creator.service.CreatorRegisterService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostListItem
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/workspace")
class WorkSpaceController(
    private val creatorProfileService: CreatorProfileService,
    private val creatorPostService: CreatorPostService,
    private val creatorQuestionService: CreatorQuestionService,
    private val creatorRegisterService: CreatorRegisterService,
) {
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ROLE_NormalUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "크리에이터 등록 신청", summary = "크리에이터 등록 신청", tags = ["workspace"], description = "크리에이터 등록 신청")
    fun registerCreator(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestBody request: @Valid RegisterCreatorRequest
    ): RegisterCreatorResponse {
        val creator = creatorRegisterService.register(userPrincipal.user, request.mainSubject, request.introduction)
        return RegisterCreatorResponse(creator.id!!)
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "크리에이터 정보 조회 (나)",
        summary = "크리에이터 정보 조회 (나)",
        tags = ["workspace"],
        description = "크리에이터 정보 조회 (나)"
    )
    fun getMyCreatorInformation(@AuthenticationPrincipal userPrincipal: UserPrincipal): CreatorProfileResponse {
        val me = userPrincipal.creator!!
        return CreatorProfileResponse(me.mainSubject, me.introduction)
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "크리에이터 정보 수정", summary = "크리에이터 정보 수정", tags = ["workspace"], description = "크리에이터 정보 수정")
    fun updateMyCreatorInformation(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid UpdateCreatorProfileRequest
    ): DefaultResponse {
        creatorProfileService.updateCreatorProfile(userPrincipal.creator!!, request.mainSubject, request.introduction)
        return success()
    }

    @GetMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "나의 자작 문제 목록 조회",
        summary = "나의 자작 문제 목록 조회",
        tags = ["workspace"],
        description = "나의 자작 문제 목록 조회"
    )
    @Parameter(name = "size", description = "paging size", schema = Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = Schema(type = "integer"))
    fun getQuestions(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<QuestionInformation> {
        val total = creatorQuestionService.countMyQuestions(userPrincipal.creator!!.id!!)
        val questions = creatorQuestionService.getMyQuestions(
            userPrincipal.creator.id!!, pagingInformation
        )
        return PagingResponse(total, questions)
    }

    @GetMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "나의 자작 문제 상세 조회",
        summary = "나의 자작 문제 상세 조회",
        tags = ["workspace"],
        description = "나의 자작 문제 상세 조회"
    )
    fun getQuestion(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable questionId: Long
    ): QuestionContentResponse {
        val questionContent = creatorQuestionService.getMyQuestionContent(userPrincipal.creator!!.id!!, questionId)
        return QuestionContentResponse(questionContent)
    }

    @PostMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "나의 자작 문제 등록", summary = "나의 자작 문제 등록", tags = ["workspace"], description = "나의 자작 문제 등록")
    fun register(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterQuestionRequest
    ): DefaultResponse {
        creatorQuestionService.registerQuestion(userPrincipal.creator!!.id!!, request.toQuestionContent())
        return success()
    }

    @PatchMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "나의 자작 문제 수정", summary = "나의 자작 문제 수정", tags = ["workspace"], description = "나의 자작 문제 수정")
    fun modify(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable questionId: Long,
        @RequestBody request: @Valid ModifyQuestionRequest
    ): DefaultResponse {
        creatorQuestionService.modifyQuestion(userPrincipal.creator!!.id!!, questionId, request.toQuestionContent())
        return success()
    }

    @DeleteMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "나의 자작 문제 삭제", summary = "나의 자작 문제 삭제", tags = ["workspace"], description = "나의 자작 문제 삭제")
    fun delete(@AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable questionId: Long): DefaultResponse {
        creatorQuestionService.deleteQuestion(userPrincipal.creator!!.id!!, questionId)
        return success()
    }

    @GetMapping("/board")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "나의 자작 문제 게시판 목록 조회", summary = "나의 자작 문제 게시판 목록 조회", tags = ["workspace"], description = """
                나의 자작 문제 게시판에 등록된 게시글들을 통합 조회 합니다.            
            """
    )
    fun creatorQuestionBoardList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<PostListItem> {
        val total = creatorPostService.countCreatorPost(userPrincipal.creator!!.id!!)
        val boards = creatorPostService.getCreatorPosts(
            userPrincipal.creator.id!!,
            pagingInformation
        )
        return PagingResponse(total, boards)
    }
}
