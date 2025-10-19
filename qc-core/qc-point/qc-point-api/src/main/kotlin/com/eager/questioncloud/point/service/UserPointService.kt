package com.eager.questioncloud.point.service

import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.implement.UserPointReader
import org.springframework.stereotype.Service

@Service
class UserPointService(
    private val userPointReader: UserPointReader
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointReader.getUserPoint(userId)
    }
}
