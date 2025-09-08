package com.eager.questioncloud.post.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.Post

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
