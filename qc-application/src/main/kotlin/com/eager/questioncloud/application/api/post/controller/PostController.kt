package com.eager.questioncloud.application.api.post.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.post.dto.ModifyPostRequest
import com.eager.questioncloud.application.api.post.dto.PostResponse
import com.eager.questioncloud.application.api.post.dto.RegisterPostRequest
import com.eager.questioncloud.application.api.post.service.PostService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostListItem
import com.eager.questioncloud.core.domain.post.model.Post.Companion.create
import com.eager.questioncloud.core.domain.post.model.PostContent.Companion.create
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {
    @PatchMapping("/{postId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시판 글 수정",
        summary = "문제 게시판 글 수정",
        tags = ["question-board"],
        description = "문제 게시판 글 수정"
    )
    fun modify(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable postId: Long,
        @RequestBody request: @Valid ModifyPostRequest
    ): DefaultResponse {
        postService.modify(
            postId,
            userPrincipal.user.uid,
            create(request.title, request.content, request.files)
        )
        return success()
    }

    @DeleteMapping("/{postId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시판 글 삭제",
        summary = "문제 게시판 글 삭제",
        tags = ["question-board"],
        description = "문제 게시판 글 삭제"
    )
    fun delete(@AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable postId: Long): DefaultResponse {
        postService.delete(postId, userPrincipal.user.uid)
        return success()
    }

    @GetMapping("/{postId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시판 글 조회",
        summary = "문제 게시판 글 조회",
        tags = ["question-board"],
        description = "문제 게시판 글 조회"
    )
    fun getPost(@AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable postId: Long): PostResponse {
        val board = postService.getPostDetail(userPrincipal.user.uid, postId)
        return PostResponse(board)
    }

    @GetMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시판 글 목록 조회",
        summary = "문제 게시판 글 목록 조회",
        tags = ["question-board"],
        description = "문제 게시판 글 목록 조회"
    )
    @Parameter(name = "size", description = "paging size", schema = Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = Schema(type = "integer"))
    fun getPosts(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam questionId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<PostListItem> {
        val total = postService.countPost(questionId)
        val boards = postService.getPostList(userPrincipal.user.uid, questionId, pagingInformation)
        return PagingResponse(total, boards)
    }

    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 게시판 글 등록",
        summary = "문제 게시판 글 등록",
        tags = ["question-board"],
        description = "문제 게시판 글 등록"
    )
    fun register(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterPostRequest
    ): DefaultResponse {
        postService.register(
            create(
                request.questionId,
                userPrincipal.user.uid,
                create(request.title, request.content, request.files)
            )
        )
        return success()
    }
}
