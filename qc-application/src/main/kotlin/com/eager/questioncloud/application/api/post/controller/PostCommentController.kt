package com.eager.questioncloud.application.api.post.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.post.dto.AddPostCommentRequest
import com.eager.questioncloud.application.api.post.dto.ModifyPostCommentRequest
import com.eager.questioncloud.application.api.post.service.PostCommentService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostCommentDetail
import com.eager.questioncloud.core.domain.post.model.PostComment.Companion.create
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post/comment")
class PostCommentController(
    private val postCommentService: PostCommentService
) {
    @GetMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시글 댓글 조회",
        summary = "문제 게시글 댓글 조회",
        tags = ["question-board-comment"],
        description = "문제 게시글 댓글 조회"
    )
    @Parameter(name = "size", description = "paging size", schema = Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = Schema(type = "integer"))
    fun getPostComments(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam postId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<PostCommentDetail> {
        val total = postCommentService.count(postId)
        val comments = postCommentService.getPostComments(
            postId,
            userPrincipal.user.uid, pagingInformation
        )
        return PagingResponse(total, comments)
    }

    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시글 댓글 작성",
        summary = "문제 게시글 댓글 작성",
        tags = ["question-board-comment"],
        description = "문제 게시글 댓글 작성"
    )
    fun addPostComment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestBody request: @Valid AddPostCommentRequest
    ): DefaultResponse {
        postCommentService.addPostComment(create(request.postId, userPrincipal.user.uid, request.comment))
        return success()
    }

    @PatchMapping("/{commentId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시글 댓글 수정",
        summary = "문제 게시글 댓글 수정",
        tags = ["question-board-comment"],
        description = "문제 게시글 댓글 수정"
    )
    fun modifyPostComment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable commentId: Long,
        @RequestBody request: @Valid ModifyPostCommentRequest
    ): DefaultResponse {
        postCommentService.modifyPostComment(commentId, userPrincipal.user.uid, request.comment)
        return success()
    }

    @DeleteMapping("/{commentId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시글 댓글 삭제",
        summary = "문제 게시글 댓글 삭제",
        tags = ["question-board-comment"],
        description = "문제 게시글 댓글 삭제"
    )
    fun deletePostComment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable commentId: Long
    ): DefaultResponse {
        postCommentService.deletePostComment(commentId, userPrincipal.user.uid)
        return success()
    }
}
