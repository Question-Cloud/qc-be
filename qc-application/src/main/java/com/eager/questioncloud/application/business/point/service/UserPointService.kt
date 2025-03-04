package com.eager.questioncloud.application.business.point.service

import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.point.model.UserPoint
import org.springframework.stereotype.Service

@Service
class UserPointService(
    private val userPointRepository: UserPointRepository
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointRepository.getUserPoint(userId)
    }
}
