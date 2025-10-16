package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.post.repository.PostRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component

@Component
class PostPermissionValidator(
    private val questionQueryAPI: QuestionQueryAPI,
    private val creatorQueryAPI: CreatorQueryAPI,
    private val postRepository: PostRepository,
) {
    fun validatePostPermission(userId: Long, questionId: Long) {
        if (isCreator(userId, questionId)) {
            return
        }
        
        if (questionQueryAPI.isOwned(userId, questionId)) {
            return
        }
        
        throw CoreException(Error.FORBIDDEN)
    }
    
    fun validateCommentPermission(userId: Long, postId: Long) {
        val post = postRepository.findById(postId)
        validatePostPermission(userId, post.questionId)
    }
    
    fun isCreator(userId: Long, questionId: Long): Boolean {
        val question = questionQueryAPI.getQuestionInformation(questionId)
        val creator = creatorQueryAPI.getCreator(question.creatorId)
        
        return creator.userId == userId
    }
}
