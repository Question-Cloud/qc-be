package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.infrastructure.repository.RefreshTokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class AuthenticationTokenParser(
    @Value("\${JWT_SECRET_KEY}") secretKey: String,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    private lateinit var secretKey: SecretKey

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
    }

    fun parseAccessToken(token: String): Claims {
        val claims = Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        if (claims.subject != "accessToken") {
            throw CoreException(Error.UNAUTHORIZED_TOKEN)
        }

        return claims
    }

    fun parseRefreshToken(token: String): Claims {
        val claims = Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        if (claims.subject != "refreshToken") {
            throw CoreException(Error.UNAUTHORIZED_TOKEN)
        }

        return claims
    }
}