package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.user.authentication.model.AuthenticationToken
import com.eager.questioncloud.user.infrastructure.repository.RefreshTokenRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class AuthenticationTokenGenerator(
    @Value("\${JWT_SECRET_KEY}") secretKey: String,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    private lateinit var secretKey: SecretKey

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createAuthenticationToken(uid: Long): AuthenticationToken {
        val accessToken = generateAccessToken(uid)
        val refreshToken = generateRefreshToken(uid)
        return AuthenticationToken.create(accessToken, refreshToken)
    }

    private fun generateAccessToken(uid: Long): String {
        val currentTime = Date()
        return Jwts.builder()
            .subject("accessToken")
            .claim("uid", uid)
            .issuedAt(currentTime)
            .expiration(Date(currentTime.time + ACCESS_TOKEN_VALID_TIME))
            .signWith(secretKey)
            .compact()
    }

    private fun generateRefreshToken(uid: Long): String {
        val currentTime = Date()

        val refreshToken = Jwts.builder()
            .subject("refreshToken")
            .claim("uid", uid)
            .issuedAt(currentTime)
            .expiration(Date(currentTime.time + REFRESH_TOKEN_VALID_TIME))
            .signWith(secretKey)
            .compact()

        refreshTokenRepository.save(refreshToken, uid)
        return refreshToken
    }

    companion object {
        const val ACCESS_TOKEN_VALID_TIME: Long = 1000 * 60L * 60L * 24L
        const val REFRESH_TOKEN_VALID_TIME: Long = 1000L * 60L * 60L * 24L * 30L
    }
}