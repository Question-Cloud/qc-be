package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.dto.PostDetail
import com.eager.questioncloud.post.dto.PostPreview
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class PostReader(
    private val postRepository: PostRepository,
    private val questionQueryAPI: QuestionQueryAPI,
    private val userQueryAPI: UserQueryAPI,
) {
    fun getPostPreviews(userId: Long, questionId: Long, pagingInformation: PagingInformation): List<PostPreview> {
        val posts = postRepository.findByQuestionIdWithPagination(questionId, pagingInformation)
        val writers = userQueryAPI.getUsers(posts.map { it.writerId }).associateBy { it.userId }
        
        val postPreviews = mutableListOf<PostPreview>()
        
        for (post in posts) {
            val writer = writers.getValue(post.writerId)
            postPreviews.add(
                PostPreview(
                    post.id,
                    post.postContent.title,
                    writer.name,
                    post.createdAt,
                )
            )
        }
        
        return postPreviews
    }
    
    fun countPost(questionId: Long): Int {
        return postRepository.countByQuestionId(questionId)
    }
    
    fun getPostDetail(userId: Long, postId: Long): PostDetail {
        val post = postRepository.findById(postId)
        val question = questionQueryAPI.getQuestionInformation(post.questionId)
        val writer = userQueryAPI.getUser(post.writerId)
        
        return PostDetail(
            post.id,
            post.questionId,
            post.postContent.title,
            post.postContent.content,
            post.postContent.files,
            question.parentCategory,
            question.childCategory,
            question.title,
            writer.name,
            post.createdAt,
        )
    }
}