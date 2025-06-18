package com.eager.questioncloud.point.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.infrastructure.repository.UserPointRepository
import org.springframework.stereotype.Component

@Component
class UserPointManager(
    private val userPointRepository: UserPointRepository
) {
    fun init(userId: Long) {
        val userPoint = UserPoint.create(userId)
        userPointRepository.save(userPoint)
    }

    fun chargePoint(userId: Long, amount: Int) {
        userPointRepository.chargePoint(userId, amount)
    }

    fun usePoint(userId: Long, amount: Int) {
        if (!userPointRepository.usePoint(userId, amount)) {
            throw CoreException(Error.NOT_ENOUGH_POINT)
        }
    }
}
