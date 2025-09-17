package com.eager.questioncloud.point.repository

import com.eager.questioncloud.point.entity.UserPointEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserPointJpaRepository : JpaRepository<UserPointEntity, Long>
