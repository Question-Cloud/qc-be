package com.eager.questioncloud.user.repository

interface RefreshTokenRepository {
    fun getByUserId(uid: Long): String?
    
    fun delete(uid: String)
    
    fun save(token: String, uid: Long)
}
