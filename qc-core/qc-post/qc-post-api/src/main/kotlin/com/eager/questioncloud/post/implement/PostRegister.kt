package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.post.command.RegisterPostCommand
import com.eager.questioncloud.post.domain.Post
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.domain.PostFile
import com.eager.questioncloud.post.repository.PostRepository
import org.springframework.stereotype.Component

@Component
class PostRegister(
    private val postPermissionChecker: PostPermissionChecker,
    private val postRepository: PostRepository,
) {
    fun register(command: RegisterPostCommand): Post {
        if (!postPermissionChecker.hasPermission(command.writerId, command.questionId)) {
            throw CoreException(Error.FORBIDDEN)
        }
        
        return postRepository.save(
            Post.create(
                command.questionId,
                command.writerId,
                PostContent.create(
                    command.postContent.title,
                    command.postContent.content,
                    command.postContent.files.map { PostFile(it.fileName, it.url) })
            )
        )
    }
}