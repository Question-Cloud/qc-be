package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.post.dto.PostDetail
import com.eager.questioncloud.core.domain.post.dto.PostListItem
import com.eager.questioncloud.core.domain.post.model.Post

interface PostRepository {
    fun getPostList(questionId: Long, pagingInformation: PagingInformation): List<PostListItem>

    fun getCreatorPostList(creatorId: Long, pagingInformation: PagingInformation): List<PostListItem>

    fun countCreatorPost(creatorId: Long): Int

    fun getPostDetail(postId: Long): PostDetail

    fun findByIdAndWriterId(postId: Long, userId: Long): Post

    fun findById(postId: Long): Post

    fun count(questionId: Long): Int

    fun save(post: Post): Post

    fun delete(post: Post)
}
