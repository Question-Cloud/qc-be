package com.eager.questioncloud.payment.point.service

import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.infrastructure.repository.UserPointRepository
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userPointRepository: UserPointRepository
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointRepository.getUserPoint(userId)
    }
}
