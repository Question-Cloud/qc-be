package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.user.repository.RefreshTokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*
import javax.crypto.SecretKey


@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class AuthenticationTokenRefresherTest(
    private val authenticationTokenRefresher: AuthenticationTokenRefresher,
    private val authenticationTokenGenerator: AuthenticationTokenGenerator,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${JWT_SECRET_KEY}") secretKeyString: String,
) : BehaviorSpec() {
    
    private lateinit var secretKey: SecretKey
    
    init {
        val keyBytes = Decoders.BASE64.decode(secretKeyString)
        secretKey = Keys.hmacShaKeyFor(keyBytes)
        
        Given("유효한 리프레시 토큰이 있을 때") {
            val userId = 1L
            val authenticationToken = authenticationTokenGenerator.createAuthenticationToken(userId)
            
            When("리프레시 토큰으로 인증 토큰을 갱신하면") {
                val newAuthenticationToken = authenticationTokenRefresher.refresh(authenticationToken.refreshToken)
                
                Then("새로운 액세스 토큰과 리프레시 토큰이 발급된다.") {
                    val newAccessTokenClaims = getClaims(newAuthenticationToken.accessToken)
                    val newRefreshToken = refreshTokenRepository.getByUserId(userId)
                    
                    newAccessTokenClaims shouldNotBe null
                    newAccessTokenClaims["uid"].toString().toLong() shouldBe userId
                    
                    newRefreshToken shouldBe newAuthenticationToken.refreshToken
                }
            }
        }
        
        Given("만료된 리프레시 토큰이 있을 때") {
            val userId = 1L
            val currentTime = Date()
            val expiredRefreshToken = Jwts.builder()
                .subject("refreshToken")
                .claim("uid", userId)
                .issuedAt(currentTime)
                .expiration(Date(currentTime.time - 1000L))
                .signWith(secretKey)
                .compact()
            
            When("만료된 리프레시 토큰으로 갱신을 시도하면") {
                Then("예외가 발생한다") {
                    shouldThrow<Exception> {
                        authenticationTokenRefresher.refresh(expiredRefreshToken)
                    }
                }
            }
        }
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