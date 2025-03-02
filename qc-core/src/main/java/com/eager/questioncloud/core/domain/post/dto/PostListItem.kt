package com.eager.questioncloud.core.domain.post.dto

import java.time.LocalDateTime

class PostListItem(
    val id: Long,
    val title: String,
    val writer: String,
    val createdAt: LocalDateTime,
) {

}
