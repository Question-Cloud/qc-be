package com.eager.questioncloud.post.infrastructure.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.PostComment

interface PostCommentRepository {
    fun save(postComment: PostComment): PostComment

    fun findByIdAndWriterId(commentId: Long, userId: Long): PostComment

    fun findByPostIdWithPagination(
        postId: Long,
        pagingInformation: PagingInformation
    ): List<PostComment>

    fun delete(postComment: PostComment)

    fun count(postId: Long): Int
}
