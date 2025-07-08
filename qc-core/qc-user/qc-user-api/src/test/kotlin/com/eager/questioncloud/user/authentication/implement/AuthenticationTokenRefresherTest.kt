package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.infrastructure.repository.RefreshTokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*
import javax.crypto.SecretKey

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationTokenRefresherTest(
    @Autowired val authenticationTokenRefresher: AuthenticationTokenRefresher,
    @Autowired val authenticationTokenGenerator: AuthenticationTokenGenerator,
    @Autowired val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${JWT_SECRET_KEY}") secretKey: String,
) {
    private lateinit var secretKey: SecretKey

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
    }

    @Test
    fun `인증 토큰을 갱신할 수 있다`() {
        //given
        val userId = 1L
        val authenticationToken = authenticationTokenGenerator.createAuthenticationToken(userId)

        //when
        val newAuthenticationToken = authenticationTokenRefresher.refresh(authenticationToken.refreshToken)

        //then
        val newAccessTokenClaims = getClaims(newAuthenticationToken.accessToken)

        Assertions.assertThat(newAccessTokenClaims["uid"].toString().toLong()).isNotNull().isEqualTo(userId)
    }

    @Test
    fun `만료 된 리프레시 토큰일 경우 갱신에 실패한다`() {
        //given
        val userId = 1L
        val currentTime = Date()
        val expiredRefreshToken = Jwts.builder()
            .subject("refreshToken")
            .claim("uid", userId)
            .issuedAt(currentTime)
            .expiration(Date(currentTime.time - 1000L))
            .signWith(secretKey)
            .compact()

        // when then
        Assertions.assertThatThrownBy { authenticationTokenRefresher.refresh(expiredRefreshToken) }
            .isInstanceOf(Exception::class.java)
    }

    private fun getClaims(token: String?): Claims {
        return Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
