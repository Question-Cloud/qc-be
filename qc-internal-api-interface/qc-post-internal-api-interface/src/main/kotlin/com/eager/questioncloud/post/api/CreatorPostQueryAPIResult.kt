package com.eager.questioncloud.post.api

import java.time.LocalDateTime

class CreatorPostQueryAPIResult(
    val id: Long,
    val writerId: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
)