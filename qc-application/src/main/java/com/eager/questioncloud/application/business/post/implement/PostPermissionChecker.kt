package com.eager.questioncloud.application.business.post.implement

import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Component

@Component
class PostPermissionChecker(
    private val questionRepository: QuestionRepository,
    private val userQuestionRepository: UserQuestionRepository,
    private val postRepository: PostRepository,
) {
    fun hasPermission(userId: Long, questionId: Long): Boolean {
        if (!questionRepository.isAvailable(questionId)) {
            throw CoreException(Error.UNAVAILABLE_QUESTION)
        }

        if (!userQuestionRepository.isOwned(userId, questionId)) {
            throw CoreException(Error.NOT_OWNED_QUESTION)
        }

        return true
    }

    fun hasCommentPermission(userId: Long, postId: Long): Boolean {
        val post = postRepository.findById(postId)
        return hasPermission(userId, post.questionId)
    }
}
