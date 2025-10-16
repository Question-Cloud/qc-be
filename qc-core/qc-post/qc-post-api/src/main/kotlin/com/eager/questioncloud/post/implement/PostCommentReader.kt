package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.dto.PostCommentDetail
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class PostCommentReader(
    private val postCommentRepository: PostCommentRepository,
    private val userQueryAPI: UserQueryAPI,
) {
    fun getPostCommentDetails(
        userId: Long,
        postId: Long,
        pagingInformation: PagingInformation
    ): List<PostCommentDetail> {
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
    
    fun count(postId: Long): Int {
        return postCommentRepository.count(postId)
    }
}