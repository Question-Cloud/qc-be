package com.eager.questioncloud.user.infrastructure.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RefreshTokenRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : RefreshTokenRepository {

    override fun getByUserId(uid: Long): String? {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + uid)
    }

    override fun delete(uid: String) {
        redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + uid)
    }

    override fun save(token: String, uid: Long) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + uid, token, Duration.ofHours(24))
    }

    companion object {
        const val REFRESH_TOKEN_KEY_PREFIX: String = "REFRESH-TOKEN-UID:"
    }
}
