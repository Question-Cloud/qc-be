package com.eager.questioncloud.point.api.internal

interface PointCommandAPI {
    fun initialize(userId: Long)

    fun usePoint(userId: Long, amount: Int)
}