package com.eager.questioncloud.post.domain

import java.time.LocalDateTime

class PostComment(
    val id: Long = 0,
    val postId: Long,
    val writerId: Long,
    var comment: String,
    val isCreator: Boolean,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun modify(comment: String) {
        this.comment = comment
    }

    companion object {
        fun create(postId: Long, writerId: Long, comment: String, isCreator: Boolean): PostComment {
            return PostComment(postId = postId, writerId = writerId, comment = comment, isCreator = isCreator)
        }
    }
}
