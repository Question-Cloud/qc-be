package com.eager.questioncloud.point.repository

import com.eager.questioncloud.point.domain.UserPoint
import org.springframework.stereotype.Repository

@Repository
interface UserPointRepository {
    fun getUserPoint(userId: Long): UserPoint
    
    fun usePoint(userId: Long, amount: Int): Boolean
    
    fun chargePoint(userId: Long, amount: Int)
    
    fun save(userPoint: UserPoint): UserPoint
    
    fun deleteAllInBatch()
}
