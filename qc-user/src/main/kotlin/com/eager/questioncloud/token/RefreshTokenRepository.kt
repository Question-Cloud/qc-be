package com.eager.questioncloud.token

interface RefreshTokenRepository {
    fun getByUserId(uid: Long): String?

    fun delete(uid: String)

    fun save(token: String, uid: Long)
}
