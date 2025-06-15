package com.eager.questioncloud.point.infrastructure.repository

import com.eager.questioncloud.point.infrastructure.entity.UserPointEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserPointJpaRepository : JpaRepository<UserPointEntity, Long>
