package com.eager.questioncloud.post.implement

import com.eager.questioncloud.post.command.ModifyPostCommand
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.domain.PostFile
import com.eager.questioncloud.post.repository.PostRepository
import org.springframework.stereotype.Component

@Component
class PostUpdater(
    private val postRepository: PostRepository,
) {
    fun modify(command: ModifyPostCommand) {
        val post = postRepository.findByIdAndWriterId(command.postId, command.userId)
        post.updateQuestionBoardContent(
            PostContent.create(
                command.postContent.title,
                command.postContent.content,
                command.postContent.files.map { PostFile(it.fileName, it.url) })
        )
        postRepository.save(post)
    }
}