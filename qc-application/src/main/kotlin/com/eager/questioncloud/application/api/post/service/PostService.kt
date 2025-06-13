package com.eager.questioncloud.application.api.post.service

import com.eager.questioncloud.application.api.post.implement.PostPermissionChecker
import com.eager.questioncloud.application.api.post.implement.PostReader
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostDetail
import com.eager.questioncloud.core.domain.post.dto.PostPreview
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.post.model.Post
import com.eager.questioncloud.core.domain.post.model.PostContent
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postPermissionChecker: PostPermissionChecker,
    private val postReader: PostReader,
    private val postRepository: PostRepository,
) {
    fun register(post: Post): Post {
        if (!postPermissionChecker.hasPermission(post.writerId, post.questionId)) {
            throw CoreException(Error.FORBIDDEN)
        }

        return postRepository.save(post)
    }

    fun getPostPreviews(userId: Long, questionId: Long, pagingInformation: PagingInformation): List<PostPreview> {
        return postReader.getPostPreviews(userId, questionId, pagingInformation)
    }

    fun countPost(questionId: Long): Int {
        return postReader.countPost(questionId)
    }

    fun getPostDetail(userId: Long, postId: Long): PostDetail {
        return postReader.getPostDetail(userId, postId)
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
