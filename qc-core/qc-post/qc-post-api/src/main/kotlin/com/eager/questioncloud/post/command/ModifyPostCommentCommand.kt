package com.eager.questioncloud.post.command

data class ModifyPostCommentCommand(
    val commentId: Long,
    val userId: Long,
    val comment: String
)
