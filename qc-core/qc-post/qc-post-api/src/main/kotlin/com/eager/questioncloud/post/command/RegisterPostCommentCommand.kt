package com.eager.questioncloud.post.command

data class RegisterPostCommentCommand(
    val postId: Long,
    val userId: Long,
    val comment: String
)