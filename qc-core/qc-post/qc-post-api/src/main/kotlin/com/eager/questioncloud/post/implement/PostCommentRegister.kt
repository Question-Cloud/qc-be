package com.eager.questioncloud.post.implement

import com.eager.questioncloud.post.command.RegisterPostCommentCommand
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import org.springframework.stereotype.Component

@Component
class PostCommentRegister(
    private val postPermissionValidator: PostPermissionValidator,
    private val postCommentRepository: PostCommentRepository,
    private val questionQueryAPI: QuestionQueryAPI,
) {
    fun register(command: RegisterPostCommentCommand): PostComment {
        val postQuestion = questionQueryAPI.getQuestionInformation(command.postId)
        val postComment = PostComment.create(
            command.postId,
            command.userId,
            command.comment,
            postPermissionValidator.isCreator(command.userId, postQuestion.id) // TODO REFACTOR!!!!
        )
        return postCommentRepository.save(postComment)
    }
}