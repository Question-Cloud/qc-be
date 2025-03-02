package com.eager.questioncloud.core.domain.point.infrastructure.repository

import com.eager.questioncloud.core.domain.point.model.UserPoint
import org.springframework.stereotype.Repository

@Repository
interface UserPointRepository {
    fun getUserPoint(userId: Long): UserPoint

    fun usePoint(userId: Long, amount: Int): Boolean

    fun chargePoint(userId: Long, amount: Int)

    fun save(userPoint: UserPoint): UserPoint

    fun deleteAllInBatch()
}
