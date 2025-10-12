package com.eager.questioncloud.post.dto

import com.eager.questioncloud.post.command.RegisterPostCommand
import com.eager.questioncloud.post.command.RegisterPostCommandPostContent
import com.eager.questioncloud.post.command.RegisterPostCommandPostFile
import com.eager.questioncloud.post.domain.PostFile
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class RegisterPostRequest(
    @NotNull val questionId: Long,
    @NotBlank val title: String,
    @NotBlank val content: String,
    @JsonSetter(nulls = Nulls.AS_EMPTY) val files: List<PostFile> = ArrayList()
) {
    fun toCommand(writerId: Long): RegisterPostCommand {
        val commandPostFile = this.files.map {
            RegisterPostCommandPostFile(it.fileName, it.url)
        }
        
        return RegisterPostCommand(
            questionId = questionId,
            writerId = writerId,
            postContent = RegisterPostCommandPostContent(
                title = title,
                content = content,
                files = commandPostFile
            )
        )
    }
}