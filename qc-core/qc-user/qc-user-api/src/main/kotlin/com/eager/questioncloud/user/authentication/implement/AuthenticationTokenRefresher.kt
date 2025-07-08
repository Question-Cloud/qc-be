package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.user.authentication.model.AuthenticationToken
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class AuthenticationTokenRefresher(
    @Value("\${JWT_SECRET_KEY}") secretKey: String,
    private val authenticationTokenGenerator: AuthenticationTokenGenerator,
    private val authenticationTokenParser: AuthenticationTokenParser
) {
    private lateinit var secretKey: SecretKey

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
    }

    fun refresh(refreshToken: String): AuthenticationToken {
        val refreshTokenClaims = authenticationTokenParser.parseRefreshToken(refreshToken)
        return authenticationTokenGenerator.createAuthenticationToken(refreshTokenClaims["uid"].toString().toLong())
    }
}