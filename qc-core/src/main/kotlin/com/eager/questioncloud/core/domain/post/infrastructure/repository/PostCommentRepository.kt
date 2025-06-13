package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.model.PostComment

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
