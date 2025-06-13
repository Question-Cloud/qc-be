package com.eager.questioncloud.application.api.post.service

import com.eager.questioncloud.application.api.post.implement.PostCommentDetailReader
import com.eager.questioncloud.application.api.post.implement.PostCommentRegister
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostCommentDetail
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostCommentRepository
import org.springframework.stereotype.Service

@Service
class PostCommentService(
    private val postCommentRegister: PostCommentRegister,
    private val postCommentDetailReader: PostCommentDetailReader,
    private val postCommentRepository: PostCommentRepository,
) {
    fun addPostComment(postId: Long, userId: Long, comment: String) {
        postCommentRegister.register(postId, userId, comment)
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

    fun getPostCommentDetails(
        postId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<PostCommentDetail> {
        return postCommentDetailReader.getPostCommentDetails(userId, postId, pagingInformation)
    }

    fun count(postId: Long): Int {
        return postCommentRepository.count(postId)
    }
}
