package com.eager.questioncloud.core.domain.post.infrastructure.entity

import com.eager.questioncloud.core.domain.post.model.Post
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post")
class PostEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
    @Column var questionId: Long,
    @Column var writerId: Long,
    @Embedded var postContentEntity: PostContentEntity,
    @Column var createdAt: LocalDateTime
) {
    fun toModel(): Post {
        return Post(id, questionId, writerId, postContentEntity.toModel(), createdAt)
    }

    companion object {
        fun from(post: Post): PostEntity {
            return PostEntity(
                post.id,
                post.questionId,
                post.writerId,
                PostContentEntity.from(post.postContent),
                post.createdAt
            )
        }
    }
}
