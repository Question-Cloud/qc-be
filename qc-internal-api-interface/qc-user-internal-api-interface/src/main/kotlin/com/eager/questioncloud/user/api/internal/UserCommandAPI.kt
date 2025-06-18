package com.eager.questioncloud.user.api.internal

interface UserCommandAPI {
    fun toCreator(userId: Long)
}