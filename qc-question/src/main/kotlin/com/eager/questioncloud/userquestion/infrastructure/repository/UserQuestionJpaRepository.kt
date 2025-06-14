package com.eager.questioncloud.userquestion.infrastructure.repository

import com.eager.questioncloud.userquestion.infrastructure.entity.UserQuestionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserQuestionJpaRepository : JpaRepository<UserQuestionEntity, Long> {
    fun existsByUserIdAndQuestionIdIn(userId: Long, questionIds: List<Long>): Boolean

    fun existsByUserIdAndQuestionId(userId: Long, questionId: Long): Boolean
}
