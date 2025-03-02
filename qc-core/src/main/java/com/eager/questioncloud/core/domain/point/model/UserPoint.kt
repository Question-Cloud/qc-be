package com.eager.questioncloud.core.domain.point.model

class UserPoint(val userId: Long, var point: Int) {
    fun charge(amount: Int) {
        this.point += amount
    }

    fun use(amount: Int) {
        this.point -= amount
    }
}
