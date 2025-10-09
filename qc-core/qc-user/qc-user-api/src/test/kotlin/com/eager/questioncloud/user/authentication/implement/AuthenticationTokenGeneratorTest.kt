package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.user.repository.RefreshTokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import javax.crypto.SecretKey

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class AuthenticationTokenGeneratorTest(
    private val authenticationTokenGenerator: AuthenticationTokenGenerator,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${JWT_SECRET_KEY}") secretKeyString: String,
) : BehaviorSpec() {
    private lateinit var secretKey: SecretKey
    
    init {
        val keyBytes = Decoders.BASE64.decode(secretKeyString)
        secretKey = Keys.hmacShaKeyFor(keyBytes)
        
        Given("사용자 ID가 주어졌을 때") {
            val userId = 1L
            
            When("인증 토큰을 생성하면") {
                val authenticationToken = authenticationTokenGenerator.createAuthenticationToken(userId)
                
                Then("액세스 토큰에 사용자 ID가 포함되어 있다") {
                    val accessTokenClaims = getClaims(authenticationToken.accessToken)
                    
                    accessTokenClaims shouldNotBe null
                    accessTokenClaims["uid"].toString().toLong() shouldBe userId
                }
                
                Then("리프레시 토큰에 사용자 ID가 포함되어 있다") {
                    val refreshTokenClaims = getClaims(authenticationToken.refreshToken)
                    
                    refreshTokenClaims shouldNotBe null
                    refreshTokenClaims["uid"].toString().toLong() shouldBe userId
                }
                
                Then("리프레시 토큰이 저장소에 저장된다") {
                    val storedRefreshToken = refreshTokenRepository.getByUserId(userId)
                    
                    authenticationToken.refreshToken shouldBe storedRefreshToken
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
