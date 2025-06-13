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
import com.eager.questioncloud.core.domain.post.dto.PostPreview
import com.eager.questioncloud.core.domain.post.model.Post.Companion.create
import com.eager.questioncloud.core.domain.post.model.PostContent.Companion.create
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {
    @PatchMapping("/{postId}")
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
    fun delete(@AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable postId: Long): DefaultResponse {
        postService.delete(postId, userPrincipal.user.uid)
        return success()
    }

    @GetMapping("/{postId}")
    fun getPost(@AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable postId: Long): PostResponse {
        val board = postService.getPostDetail(userPrincipal.user.uid, postId)
        return PostResponse(board)
    }

    @GetMapping
    fun getPosts(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam questionId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<PostPreview> {
        val total = postService.countPost(questionId)
        val boards = postService.getPostPreviews(userPrincipal.user.uid, questionId, pagingInformation)
        return PagingResponse(total, boards)
    }

    @PostMapping
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
