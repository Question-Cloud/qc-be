package com.eager.questioncloud.application.api.post.service

import com.eager.questioncloud.application.api.post.implement.PostPermissionChecker
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostDetail
import com.eager.questioncloud.core.domain.post.dto.PostListItem
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.post.model.Post
import com.eager.questioncloud.core.domain.post.model.PostContent
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postPermissionChecker: PostPermissionChecker,
    private val postRepository: PostRepository,
) {
    fun register(post: Post): Post {
        if (!postPermissionChecker.hasPermission(post.writerId, post.questionId)) {
            throw CoreException(Error.FORBIDDEN)
        }

        return postRepository.save(post)
    }

    fun getPostList(userId: Long, questionId: Long, pagingInformation: PagingInformation): List<PostListItem> {
        if (!postPermissionChecker.hasPermission(userId, questionId)) {
            throw CoreException(Error.FORBIDDEN)
        }

        return postRepository.getPostList(questionId, pagingInformation)
    }

    fun countPost(questionId: Long): Int {
        return postRepository.count(questionId)
    }

    fun getPostDetail(userId: Long, postId: Long): PostDetail {
        val post = postRepository.getPostDetail(postId)
        if (!postPermissionChecker.hasPermission(userId, post.questionId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        return post
    }

    fun modify(postId: Long, userId: Long, postContent: PostContent) {
        val post = postRepository.findByIdAndWriterId(postId, userId)
        post.updateQuestionBoardContent(postContent)
        postRepository.save(post)
    }

    fun delete(postId: Long, userId: Long) {
        val post = postRepository.findByIdAndWriterId(postId, userId)
        postRepository.delete(post)
    }
}
