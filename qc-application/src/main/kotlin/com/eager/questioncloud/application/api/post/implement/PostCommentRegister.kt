package com.eager.questioncloud.application.api.post.implement

import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostCommentRepository
import com.eager.questioncloud.core.domain.post.model.PostComment
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Component

@Component
class PostCommentRegister(
    private val postPermissionChecker: PostPermissionChecker,
    private val postCommentRepository: PostCommentRepository,
    private val questionRepository: QuestionRepository,
) {
    fun register(postId: Long, userId: Long, comment: String): PostComment {
        if (!postPermissionChecker.hasCommentPermission(userId, postId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        val postQuestion = questionRepository.get(postId)
        val postComment =
            PostComment.create(postId, userId, comment, postPermissionChecker.isCreator(userId, postQuestion.id))
        return postCommentRepository.save(postComment)
    }
}