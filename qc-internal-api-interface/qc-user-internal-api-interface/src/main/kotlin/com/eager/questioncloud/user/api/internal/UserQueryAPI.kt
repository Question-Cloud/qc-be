package com.eager.questioncloud.user.api.internal

interface UserQueryAPI {
    fun getUser(userId: Long): UserQueryData

    fun getUsers(userIds: List<Long>): List<UserQueryData>
}