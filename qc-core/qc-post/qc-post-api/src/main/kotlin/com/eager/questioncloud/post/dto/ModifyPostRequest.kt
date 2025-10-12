package com.eager.questioncloud.post.dto

import com.eager.questioncloud.post.command.ModifyPostCommand
import com.eager.questioncloud.post.command.ModifyPostCommandPostContent
import com.eager.questioncloud.post.command.ModifyPostCommandPostFile
import com.eager.questioncloud.post.domain.PostFile
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import jakarta.validation.constraints.NotBlank

class ModifyPostRequest(
    @NotBlank val title: String,
    @NotBlank val content: String,
    @JsonSetter(nulls = Nulls.AS_EMPTY) val files: List<PostFile> = emptyList(),
) {
    fun toCommand(postId: Long, userId: Long): ModifyPostCommand {
        val commandPostFile = this.files.map {
            ModifyPostCommandPostFile(it.fileName, it.url)
        }
        
        return ModifyPostCommand(
            postId = postId,
            userId = userId,
            postContent = ModifyPostCommandPostContent(
                title = title,
                content = content,
                files = commandPostFile
            )
        )
    }
}