package com.eager.questioncloud.post.domain

import java.time.LocalDateTime

class Post(
    val id: Long = 0,
    val questionId: Long,
    val writerId: Long,
    var postContent: PostContent,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun updateQuestionBoardContent(postContent: PostContent) {
        this.postContent = postContent
    }

    companion object {
        fun create(questionId: Long, writerId: Long, postContent: PostContent): Post {
            return Post(questionId = questionId, writerId = writerId, postContent = postContent)
        }
    }
}
