package com.eager.questioncloud.post.implement

import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component

@Component
class PostPermissionChecker(
    private val questionQueryAPI: QuestionQueryAPI,
    private val creatorQueryAPI: CreatorQueryAPI,
    private val postRepository: PostRepository,
) {
    fun hasPermission(userId: Long, questionId: Long): Boolean {
        if (isCreator(userId, questionId)) {
            return true
        }
        
        if (questionQueryAPI.isOwned(userId, questionId)) {
            return true
        }
        
        return false
    }
    
    fun hasCommentPermission(userId: Long, postId: Long): Boolean {
        val post = postRepository.findById(postId)
        return hasPermission(userId, post.questionId)
    }
    
    fun isCreator(userId: Long, questionId: Long): Boolean {
        val question = questionQueryAPI.getQuestionInformation(questionId)
        val creator = creatorQueryAPI.getCreator(question.creatorId)
        
        return creator.userId == userId
    }
}
