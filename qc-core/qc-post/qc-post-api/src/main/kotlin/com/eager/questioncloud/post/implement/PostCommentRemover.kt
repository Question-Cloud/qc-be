package com.eager.questioncloud.post.implement

import com.eager.questioncloud.post.command.DeletePostCommentCommand
import com.eager.questioncloud.post.repository.PostCommentRepository
import org.springframework.stereotype.Component

@Component
class PostCommentRemover(
    private val postCommentRepository: PostCommentRepository
) {
    fun deletePostComment(command: DeletePostCommentCommand) {
        val postComment = postCommentRepository.findByIdAndWriterId(command.commentId, command.userId)
        postCommentRepository.delete(postComment)
    }
}