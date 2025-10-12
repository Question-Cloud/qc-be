package com.eager.questioncloud.post.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.command.DeletePostCommentCommand
import com.eager.questioncloud.post.command.ModifyPostCommentCommand
import com.eager.questioncloud.post.command.RegisterPostCommentCommand
import com.eager.questioncloud.post.dto.AddPostCommentRequest
import com.eager.questioncloud.post.dto.ModifyPostCommentRequest
import com.eager.questioncloud.post.dto.PostCommentDetail
import com.eager.questioncloud.post.service.PostCommentService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post/comment")
class PostCommentController(
    private val postCommentService: PostCommentService
) {
    @GetMapping
    fun getPostComments(
        userPrincipal: UserPrincipal,
        @RequestParam postId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<PostCommentDetail> {
        val total = postCommentService.count(postId)
        val comments = postCommentService.getPostCommentDetails(
            postId,
            userPrincipal.userId, pagingInformation
        )
        return PagingResponse(total, comments)
    }
    
    @PostMapping
    fun addPostComment(
        userPrincipal: UserPrincipal, @RequestBody request: @Valid AddPostCommentRequest
    ): DefaultResponse {
        postCommentService.addPostComment(
            RegisterPostCommentCommand(request.postId, userPrincipal.userId, request.comment)
        )
        return DefaultResponse.success()
    }
    
    @PatchMapping("/{commentId}")
    fun modifyPostComment(
        userPrincipal: UserPrincipal, @PathVariable commentId: Long,
        @RequestBody request: @Valid ModifyPostCommentRequest
    ): DefaultResponse {
        postCommentService.modifyPostComment(
            ModifyPostCommentCommand(commentId, userPrincipal.userId, request.comment)
        )
        return DefaultResponse.success()
    }
    
    @DeleteMapping("/{commentId}")
    fun deletePostComment(
        userPrincipal: UserPrincipal,
        @PathVariable commentId: Long
    ): DefaultResponse {
        postCommentService.deletePostComment(
            DeletePostCommentCommand(commentId, userPrincipal.userId)
        )
        return DefaultResponse.success()
    }
}
