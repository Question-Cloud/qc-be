package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.model.Post

interface PostRepository {
    fun findByQuestionIdWithPagination(questionId: Long, pagingInformation: PagingInformation): List<Post>

    fun findByQuestionIdInWithPagination(questionIds: List<Long>, pagingInformation: PagingInformation): List<Post>

    fun countByQuestionIdIn(questionIds: List<Long>): Int

    fun findByIdAndWriterId(postId: Long, userId: Long): Post

    fun findById(postId: Long): Post

    fun countByQuestionId(questionId: Long): Int

    fun save(post: Post): Post

    fun delete(post: Post)
}
