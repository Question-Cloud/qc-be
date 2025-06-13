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
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post/comment")
class PostCommentController(
    private val postCommentService: PostCommentService
) {
    @GetMapping
    fun getPostComments(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam postId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<PostCommentDetail> {
        val total = postCommentService.count(postId)
        val comments = postCommentService.getPostCommentDetails(
            postId,
            userPrincipal.user.uid, pagingInformation
        )
        return PagingResponse(total, comments)
    }

    @PostMapping
    fun addPostComment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestBody request: @Valid AddPostCommentRequest
    ): DefaultResponse {
        postCommentService.addPostComment(request.postId, userPrincipal.user.uid, request.comment)
        return success()
    }

    @PatchMapping("/{commentId}")
    fun modifyPostComment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable commentId: Long,
        @RequestBody request: @Valid ModifyPostCommentRequest
    ): DefaultResponse {
        postCommentService.modifyPostComment(commentId, userPrincipal.user.uid, request.comment)
        return success()
    }

    @DeleteMapping("/{commentId}")
    fun deletePostComment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable commentId: Long
    ): DefaultResponse {
        postCommentService.deletePostComment(commentId, userPrincipal.user.uid)
        return success()
    }
}
