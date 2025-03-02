package com.eager.questioncloud.core.domain.point.implement

import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.point.model.UserPoint
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
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
