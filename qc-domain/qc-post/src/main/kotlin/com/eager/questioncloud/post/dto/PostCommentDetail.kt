package com.eager.questioncloud.post.dto

import java.time.LocalDateTime

class PostCommentDetail(
    val id: Long?,
    val writerName: String?,
    val profileImage: String?,
    val comment: String?,
    val isCreator: Boolean?,
    val isWriter: Boolean?,
    val createdAt: LocalDateTime?,
)
