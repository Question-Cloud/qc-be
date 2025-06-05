package com.eager.questioncloud.application.api.authentication.implement

import com.eager.questioncloud.core.domain.token.RefreshTokenRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
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
class AuthenticationTokenManagerTest(
    @Autowired val authenticationTokenManager: AuthenticationTokenManager,
    @Autowired val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${JWT_SECRET_KEY}") secretKey: String,
) {
    private lateinit var secretKey: SecretKey

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
    }

    @Test
    fun `인증 토큰을 생성할 수 있다`() {
        //given
        val userId = 1L

        // when
        val authenticationToken = authenticationTokenManager.create(userId)

        // then
        val accessTokenClaims = getClaims(authenticationToken.accessToken)
        val refreshTokenClaims = getClaims(authenticationToken.refreshToken)
        val storedRefreshToken = refreshTokenRepository.getByUserId(userId)

        Assertions.assertThat(accessTokenClaims["uid"].toString().toLong()).isNotNull().isEqualTo(userId)
        Assertions.assertThat(refreshTokenClaims["uid"].toString().toLong()).isNotNull().isEqualTo(userId)
        Assertions.assertThat(authenticationToken.refreshToken).isEqualTo(storedRefreshToken)
    }

    @Test
    fun `인증 토큰을 갱신할 수 있다`() {
        //given
        val userId = 1L
        val authenticationToken = authenticationTokenManager.create(userId)

        //when
        val newAuthenticationToken = authenticationTokenManager.refresh(authenticationToken.refreshToken)

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
        Assertions.assertThatThrownBy { authenticationTokenManager.refresh(expiredRefreshToken) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.UNAUTHORIZED_TOKEN)
    }

    @Test
    fun `accessToken에서 UID를 가져올 수 있다`() {
        //given
        val userId = 1L
        val accessToken = Jwts.builder()
            .subject("accessToken")
            .claim("uid", userId)
            .issuedAt(Date())
            .expiration(Date(Date().time + AuthenticationTokenManager.ACCESS_TOKEN_VALID_TIME))
            .signWith(secretKey)
            .compact()

        //when
        val parsedUserId = authenticationTokenManager.parseUidFromAccessToken(accessToken)

        //then
        Assertions.assertThat(parsedUserId).isEqualTo(userId)
    }

    private fun getClaims(token: String?): Claims {
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
}