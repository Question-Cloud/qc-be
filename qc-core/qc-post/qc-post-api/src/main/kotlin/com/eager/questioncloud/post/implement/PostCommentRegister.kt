package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.post.command.RegisterPostCommentCommand
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component

@Component
class PostCommentRegister(
    private val postPermissionChecker: PostPermissionChecker,
    private val postCommentRepository: PostCommentRepository,
    private val questionQueryAPI: QuestionQueryAPI,
) {
    fun register(command: RegisterPostCommentCommand): PostComment {
        if (!postPermissionChecker.hasCommentPermission(command.userId, command.postId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        val postQuestion = questionQueryAPI.getQuestionInformation(command.postId)
        val postComment = PostComment.create(
            command.postId,
            command.userId,
            command.comment,
            postPermissionChecker.isCreator(command.userId, postQuestion.id)
        )
        return postCommentRepository.save(postComment)
    }
}