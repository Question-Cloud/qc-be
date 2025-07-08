package com.eager.questioncloud.point.api.internal

import com.eager.questioncloud.point.implement.UserPointManager
import org.springframework.stereotype.Component

@Component
class PointCommandAPIImpl(
    private val userPointManager: UserPointManager
) : PointCommandAPI {
    override fun initialize(userId: Long) {
        userPointManager.init(userId)
    }

    override fun usePoint(userId: Long, amount: Int) {
        userPointManager.usePoint(userId, amount)
    }
}