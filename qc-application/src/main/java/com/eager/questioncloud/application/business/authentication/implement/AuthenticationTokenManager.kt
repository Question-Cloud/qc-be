package com.eager.questioncloud.application.business.authentication.implement

import com.eager.questioncloud.application.api.authentication.dto.AuthenticationToken
import com.eager.questioncloud.core.domain.token.RefreshTokenRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class AuthenticationTokenManager(
    @Value("\${JWT_SECRET_KEY}") secretKey: String,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    private lateinit var secretKey: SecretKey

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
    }

    fun create(uid: Long): AuthenticationToken {
        val accessToken = generateAccessToken(uid)
        val refreshToken = generateRefreshToken(uid)
        return AuthenticationToken.create(accessToken, refreshToken)
    }

    fun refresh(refreshToken: String): AuthenticationToken {
        val claims = getRefreshTokenClaimsWithValidate(refreshToken)
        val uid = claims.get("uid").toString().toLong()
        val accessToken = generateAccessToken(uid)
        if (isExpireSoon(claims.expiration)) {
            return AuthenticationToken.create(accessToken, generateRefreshToken(uid))
        }
        return AuthenticationToken.create(accessToken, refreshToken)
    }

    fun parseUidFromAccessToken(accessToken: String?): Long {
        val claims = getClaims(accessToken)

        if (claims.subject != "accessToken") {
            throw CoreException(Error.UNAUTHORIZED_TOKEN)
        }

        return claims.get("uid").toString().toLong()
    }

    fun getClaims(token: String?): Claims {
        try {
            return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            throw CoreException(Error.UNAUTHORIZED_TOKEN)
        }
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

    private fun getRefreshTokenClaimsWithValidate(refreshToken: String): Claims {
        val claims = getClaims(refreshToken)

        if (claims.subject != "refreshToken") {
            throw CoreException(Error.UNAUTHORIZED_TOKEN)
        }

        val uid = claims.get("uid").toString().toLong()
        val refreshTokenInStore = refreshTokenRepository.getByUserId(uid)

        if (refreshToken != refreshTokenInStore) {
            throw CoreException(Error.UNAUTHORIZED_TOKEN)
        }

        return claims
    }

    private fun isExpireSoon(expireDate: Date): Boolean {
        val now = Date()
        val differenceInMillis = expireDate.time - now.time
        val differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24)
        return differenceInDays <= 7
    }

    companion object {
        const val ACCESS_TOKEN_VALID_TIME: Long = 1000 * 60L * 60L * 24L
        const val REFRESH_TOKEN_VALID_TIME: Long = 1000L * 60L * 60L * 24L * 30L
    }
}
