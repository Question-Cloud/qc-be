package com.eager.questioncloud.post.implement

import com.eager.questioncloud.post.command.DeletePostCommand
import com.eager.questioncloud.post.repository.PostRepository
import org.springframework.stereotype.Component

@Component
class PostRemover(
    private val postRepository: PostRepository,
) {
    fun delete(command: DeletePostCommand) {
        val post = postRepository.findByIdAndWriterId(command.postId, command.userId)
        postRepository.delete(post)
    }
}