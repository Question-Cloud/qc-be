package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.infrastructure.entity.CreatorEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CreatorJpaRepository : JpaRepository<CreatorEntity, Long>
