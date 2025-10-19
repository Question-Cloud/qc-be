package com.eager.questioncloud.point.implement

import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.repository.UserPointRepository
import org.springframework.stereotype.Component

@Component
class UserPointReader(
    private val userPointRepository: UserPointRepository,
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointRepository.getUserPoint(userId)
    }
}