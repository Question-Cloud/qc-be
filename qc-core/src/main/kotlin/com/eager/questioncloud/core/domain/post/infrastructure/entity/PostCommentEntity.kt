package com.eager.questioncloud.core.domain.post.infrastructure.entity

import com.eager.questioncloud.core.domain.post.model.PostComment
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post_comment")
class PostCommentEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
    @Column var postId: Long,
    @Column var writerId: Long,
    @Column var comment: String,
    @Column var isCreator: Boolean,
    @Column var createdAt: LocalDateTime
) {
    fun toModel(): PostComment {
        return PostComment(id, postId, writerId, comment, isCreator, createdAt)
    }

    companion object {
        fun from(postComment: PostComment): PostCommentEntity {
            return PostCommentEntity(
                postComment.id,
                postComment.postId,
                postComment.writerId,
                postComment.comment,
                postComment.isCreator,
                postComment.createdAt
            )
        }
    }
}
