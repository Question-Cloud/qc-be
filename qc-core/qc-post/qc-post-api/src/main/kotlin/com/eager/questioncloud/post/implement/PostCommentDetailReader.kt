package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.dto.PostCommentDetail
import com.eager.questioncloud.post.infrastructure.repository.PostCommentRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class PostCommentDetailReader(
    private val postPermissionChecker: PostPermissionChecker,
    private val postCommentRepository: PostCommentRepository,
    private val userQueryAPI: UserQueryAPI,
) {
    fun getPostCommentDetails(
        userId: Long,
        postId: Long,
        pagingInformation: PagingInformation
    ): List<PostCommentDetail> {
        if (!postPermissionChecker.hasCommentPermission(userId, postId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        val postComments = postCommentRepository.findByPostIdWithPagination(postId, pagingInformation)
        val writerIds = postComments.map { it.writerId }
        val writers = userQueryAPI.getUsers(writerIds).associateBy { it.userId }

        val postCommentDetails = mutableListOf<PostCommentDetail>()

        for (postComment in postComments) {
            val writer = writers.getValue(postComment.writerId)
            postCommentDetails.add(
                PostCommentDetail(
                    postComment.id,
                    writer.name,
                    writer.profileImage,
                    postComment.comment,
                    postComment.isCreator,
                    writer.userId == userId,
                    postComment.createdAt
                )
            )
        }

        return postCommentDetails
    }
}