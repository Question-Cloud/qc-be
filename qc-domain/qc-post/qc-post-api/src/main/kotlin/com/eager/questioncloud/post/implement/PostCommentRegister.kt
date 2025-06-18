package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.infrastructure.repository.PostCommentRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component

@Component
class PostCommentRegister(
    private val postPermissionChecker: PostPermissionChecker,
    private val postCommentRepository: PostCommentRepository,
    private val questionQueryAPI: QuestionQueryAPI,
) {
    fun register(postId: Long, userId: Long, comment: String): PostComment {
        if (!postPermissionChecker.hasCommentPermission(userId, postId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        val postQuestion = questionQueryAPI.getQuestionInformation(postId)
        val postComment =
            PostComment.create(postId, userId, comment, postPermissionChecker.isCreator(userId, postQuestion.id))
        return postCommentRepository.save(postComment)
    }
}