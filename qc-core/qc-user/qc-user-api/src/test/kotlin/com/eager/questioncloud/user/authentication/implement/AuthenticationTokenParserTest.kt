package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.repository.RefreshTokenRepository
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
class AuthenticationTokenParserTest(
    @Autowired val authenticationTokenParser: AuthenticationTokenParser,
    @Autowired val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${JWT_SECRET_KEY}") secretKey: String,
) {
    private lateinit var secretKey: SecretKey
    
    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
    }
    
    @Test
    fun `accessToken에서 UID를 가져올 수 있다`() {
        //given
        val userId = 1L
        val accessToken = Jwts.builder()
            .subject("accessToken")
            .claim("uid", userId)
            .issuedAt(Date())
            .expiration(Date(Date().time + AuthenticationTokenGenerator.ACCESS_TOKEN_VALID_TIME))
            .signWith(secretKey)
            .compact()
        
        //when
        val claims = authenticationTokenParser.parseAccessToken(accessToken)
        
        //then
        Assertions.assertThat(claims["uid"].toString().toLong()).isEqualTo(userId)
    }
    
    @Test
    fun `만료된 accessToken 파싱 시 예외가 발생한다`() {
        //given
        val userId = 1L
        val currentTime = Date()
        val expiredAccessToken = Jwts.builder()
            .subject("accessToken")
            .claim("uid", userId)
            .issuedAt(currentTime)
            .expiration(Date(currentTime.time - 1000L))
            .signWith(secretKey)
            .compact()
        
        // when then
        Assertions.assertThatThrownBy { authenticationTokenParser.parseAccessToken(expiredAccessToken) }
            .isInstanceOf(Exception::class.java)
    }
    
    @Test
    fun `잘못된 subject를 가진 토큰 파싱 시 예외가 발생한다`() {
        //given
        val userId = 1L
        val invalidToken = Jwts.builder()
            .subject("invalidSubject")
            .claim("uid", userId)
            .issuedAt(Date())
            .expiration(Date(Date().time + AuthenticationTokenGenerator.ACCESS_TOKEN_VALID_TIME))
            .signWith(secretKey)
            .compact()
        
        // when then
        Assertions.assertThatThrownBy { authenticationTokenParser.parseAccessToken(invalidToken) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.UNAUTHORIZED_TOKEN)
    }
}
