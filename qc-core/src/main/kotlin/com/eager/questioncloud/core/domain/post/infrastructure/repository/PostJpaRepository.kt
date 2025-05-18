package com.eager.questioncloud.core.domain.post.infrastructure.repository

import com.eager.questioncloud.core.domain.post.infrastructure.entity.PostEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PostJpaRepository : JpaRepository<PostEntity, Long> {
    fun findByIdAndWriterId(id: Long, userId: Long): Optional<PostEntity>
}
