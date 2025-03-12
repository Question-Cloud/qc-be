package com.eager.questioncloud.application.api.post.service

import com.eager.questioncloud.application.api.post.implement.PostPermissionChecker
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostCommentDetail
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostCommentRepository
import com.eager.questioncloud.core.domain.post.model.PostComment
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Service

@Service
class PostCommentService(
    private val postPermissionChecker: PostPermissionChecker,
    private val postCommentRepository: PostCommentRepository,
) {
    fun addPostComment(postComment: PostComment): PostComment {
        if (!postPermissionChecker.hasCommentPermission(postComment.writerId, postComment.postId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        return postCommentRepository.save(postComment)
    }

    fun modifyPostComment(commentId: Long, userId: Long, comment: String) {
        val postComment = postCommentRepository.findByIdAndWriterId(commentId, userId)
        postComment.modify(comment)
        postCommentRepository.save(postComment)
    }

    fun deletePostComment(commentId: Long, userId: Long) {
        val postComment = postCommentRepository.findByIdAndWriterId(commentId, userId)
        postCommentRepository.delete(postComment)
    }

    fun getPostComments(postId: Long, userId: Long, pagingInformation: PagingInformation): List<PostCommentDetail> {
        if (!postPermissionChecker.hasCommentPermission(userId, postId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        return postCommentRepository.getPostCommentDetails(postId, userId, pagingInformation)
    }

    fun count(postId: Long): Int {
        return postCommentRepository.count(postId)
    }
}
