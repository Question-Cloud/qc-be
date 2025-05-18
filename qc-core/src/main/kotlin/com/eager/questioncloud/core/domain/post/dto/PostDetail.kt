package com.eager.questioncloud.core.domain.post.dto

import com.eager.questioncloud.core.domain.post.model.PostFile
import java.time.LocalDateTime

class PostDetail(
    val id: Long,
    val questionId: Long,
    val title: String,
    val content: String,
    val files: List<PostFile>,
    val parentCategory: String,
    val childCategory: String,
    val questionTitle: String,
    val writer: String,
    val createdAt: LocalDateTime,
) {

}
