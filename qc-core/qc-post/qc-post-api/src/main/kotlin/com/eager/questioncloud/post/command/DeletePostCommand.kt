package com.eager.questioncloud.post.command

data class DeletePostCommand(
    val postId: Long,
    val userId: Long
)
