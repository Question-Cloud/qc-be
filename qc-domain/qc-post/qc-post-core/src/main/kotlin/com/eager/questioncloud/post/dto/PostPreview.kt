package com.eager.questioncloud.post.dto

import java.time.LocalDateTime

class PostPreview(
    val id: Long,
    val title: String,
    val writer: String,
    val createdAt: LocalDateTime,
)
