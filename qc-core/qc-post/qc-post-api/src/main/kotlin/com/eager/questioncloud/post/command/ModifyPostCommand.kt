package com.eager.questioncloud.post.command

data class ModifyPostCommand(
    val postId: Long,
    val userId: Long,
    val postContent: ModifyPostCommandPostContent
)

data class ModifyPostCommandPostContent(
    val title: String,
    val content: String,
    val files: List<ModifyPostCommandPostFile>
)

data class ModifyPostCommandPostFile(
    val fileName: String,
    val url: String,
)