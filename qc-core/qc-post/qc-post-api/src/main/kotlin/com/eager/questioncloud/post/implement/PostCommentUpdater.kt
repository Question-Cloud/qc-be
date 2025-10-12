package com.eager.questioncloud.post.implement

import com.eager.questioncloud.post.command.ModifyPostCommentCommand
import com.eager.questioncloud.post.repository.PostCommentRepository
import org.springframework.stereotype.Component

@Component
class PostCommentUpdater(
    private val postCommentRepository: PostCommentRepository,
) {
    fun modify(command: ModifyPostCommentCommand) {
        val postComment = postCommentRepository.findByIdAndWriterId(command.commentId, command.userId)
        postComment.modify(command.comment)
        postCommentRepository.save(postComment)
    }
}