package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.user.repository.RefreshTokenRepository
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
import javax.crypto.SecretKey

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationTokenGeneratorTest(
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
    fun `인증 토큰을 생성할 수 있다`() {
        //given
        val userId = 1L
        
        // when
        val authenticationToken = authenticationTokenGenerator.createAuthenticationToken(userId)
        
        // then
        val accessTokenClaims = getClaims(authenticationToken.accessToken)
        val refreshTokenClaims = getClaims(authenticationToken.refreshToken)
        val storedRefreshToken = refreshTokenRepository.getByUserId(userId)
        
        Assertions.assertThat(accessTokenClaims["uid"].toString().toLong()).isNotNull().isEqualTo(userId)
        Assertions.assertThat(refreshTokenClaims["uid"].toString().toLong()).isNotNull().isEqualTo(userId)
        Assertions.assertThat(authenticationToken.refreshToken).isEqualTo(storedRefreshToken)
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
