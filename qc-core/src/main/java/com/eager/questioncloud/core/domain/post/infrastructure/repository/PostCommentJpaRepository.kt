package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.domain.post.infrastructure.entity.PostCommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PostCommentJpaRepository : JpaRepository<PostCommentEntity, Long> {
    fun findByIdAndWriterId(id: Long, userId: Long): Optional<PostCommentEntity>
}
