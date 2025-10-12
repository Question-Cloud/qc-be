package com.eager.questioncloud.post.command

data class RegisterPostCommand(
    val questionId: Long,
    val writerId: Long,
    val postContent: RegisterPostCommandPostContent
)

data class RegisterPostCommandPostContent(
    val title: String,
    val content: String,
    val files: List<RegisterPostCommandPostFile>
)

data class RegisterPostCommandPostFile(
    val fileName: String,
    val url: String,
)