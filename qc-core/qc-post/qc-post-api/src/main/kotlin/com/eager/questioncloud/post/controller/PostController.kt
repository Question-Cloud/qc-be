package com.eager.questioncloud.post.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.command.DeletePostCommand
import com.eager.questioncloud.post.dto.ModifyPostRequest
import com.eager.questioncloud.post.dto.PostPreview
import com.eager.questioncloud.post.dto.PostResponse
import com.eager.questioncloud.post.dto.RegisterPostRequest
import com.eager.questioncloud.post.service.PostService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {
    @PatchMapping("/{postId}")
    fun modify(
        userPrincipal: UserPrincipal,
        @PathVariable postId: Long,
        @RequestBody request: @Valid ModifyPostRequest
    ): DefaultResponse {
        postService.modify(request.toCommand(postId, userPrincipal.userId))
        return DefaultResponse.success()
    }
    
    @DeleteMapping("/{postId}")
    fun delete(userPrincipal: UserPrincipal, @PathVariable postId: Long): DefaultResponse {
        postService.delete(DeletePostCommand(postId, userPrincipal.userId))
        return DefaultResponse.success()
    }
    
    @GetMapping("/{postId}")
    fun getPost(userPrincipal: UserPrincipal, @PathVariable postId: Long): PostResponse {
        val board = postService.getPostDetail(userPrincipal.userId, postId)
        return PostResponse(board)
    }
    
    @GetMapping
    fun getPosts(
        userPrincipal: UserPrincipal,
        @RequestParam questionId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<PostPreview> {
        val total = postService.countPost(questionId)
        val boards = postService.getPostPreviews(userPrincipal.userId, questionId, pagingInformation)
        return PagingResponse(total, boards)
    }
    
    @PostMapping
    fun register(
        userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterPostRequest
    ): DefaultResponse {
        postService.register(request.toCommand(userPrincipal.userId))
        return DefaultResponse.success()
    }
}
