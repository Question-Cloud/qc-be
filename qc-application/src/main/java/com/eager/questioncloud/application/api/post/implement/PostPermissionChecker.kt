package com.eager.questioncloud.application.api.post.implement

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import org.springframework.stereotype.Component

@Component
class PostPermissionChecker(
    private val questionRepository: QuestionRepository,
    private val creatorRepository: CreatorRepository,
    private val userQuestionRepository: UserQuestionRepository,
    private val postRepository: PostRepository,
) {
    fun hasPermission(userId: Long, questionId: Long): Boolean {
        if (isCreator(userId, questionId)) {
            return true
        }

        if (userQuestionRepository.isOwned(userId, questionId)) {
            return true
        }

        return false
    }

    fun hasCommentPermission(userId: Long, postId: Long): Boolean {
        val post = postRepository.findById(postId)
        return hasPermission(userId, post.questionId)
    }

    fun isCreator(userId: Long, questionId: Long): Boolean {
        val question = questionRepository.get(questionId)
        val creator = creatorRepository.findById(question.creatorId)

        return creator.userId == userId
    }
}
