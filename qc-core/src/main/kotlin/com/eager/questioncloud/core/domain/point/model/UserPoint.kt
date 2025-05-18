package com.eager.questioncloud.core.domain.point.model

class UserPoint(
    val userId: Long,
    var point: Int = 0
) {
    companion object {
        fun create(userId: Long): UserPoint {
            return UserPoint(userId = userId)
        }
    }

    fun charge(amount: Int) {
        this.point += amount
    }

    fun use(amount: Int) {
        this.point -= amount
    }
}
