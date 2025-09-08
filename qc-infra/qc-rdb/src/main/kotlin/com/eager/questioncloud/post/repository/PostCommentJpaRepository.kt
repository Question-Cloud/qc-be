package com.eager.questioncloud.post.repository

import com.eager.questioncloud.post.entity.PostCommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PostCommentJpaRepository : JpaRepository<PostCommentEntity, Long> {
    fun findByIdAndWriterId(id: Long, userId: Long): Optional<PostCommentEntity>
}
