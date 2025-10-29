package com.eager.questioncloud.workspace.dto

import java.time.LocalDateTime

class CreatorPostItem(
    val id: Long,
    val title: String,
    val writer: String,
    val createdAt: LocalDateTime,
)