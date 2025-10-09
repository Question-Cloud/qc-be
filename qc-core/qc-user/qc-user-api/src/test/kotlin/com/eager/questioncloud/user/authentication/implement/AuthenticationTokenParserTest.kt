package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*
import javax.crypto.SecretKey

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class AuthenticationTokenParserTest(
    private val authenticationTokenParser: AuthenticationTokenParser,
    @Value("\${JWT_SECRET_KEY}") secretKeyString: String,
) : BehaviorSpec() {
    
    private lateinit var secretKey: SecretKey
    
    init {
        val keyBytes = Decoders.BASE64.decode(secretKeyString)
        secretKey = Keys.hmacShaKeyFor(keyBytes)
        
        Given("유효한 액세스 토큰이 있을 때") {
            val userId = 1L
            val accessToken = Jwts.builder()
                .subject("accessToken")
                .claim("uid", userId)
                .issuedAt(Date())
                .expiration(Date(Date().time + AuthenticationTokenGenerator.ACCESS_TOKEN_VALID_TIME))
                .signWith(secretKey)
                .compact()
            
            When("액세스 토큰을 파싱하면") {
                val claims = authenticationTokenParser.parseAccessToken(accessToken)
                
                Then("UID를 가져올 수 있다") {
                    claims["uid"].toString().toLong() shouldBe userId
                }
            }
        }
        
        Given("만료된 액세스 토큰이 있을 때") {
            val userId = 1L
            val currentTime = Date()
            val expiredAccessToken = Jwts.builder()
                .subject("accessToken")
                .claim("uid", userId)
                .issuedAt(currentTime)
                .expiration(Date(currentTime.time - 1000L))
                .signWith(secretKey)
                .compact()
            
            When("만료된 액세스 토큰을 파싱하면") {
                Then("예외가 발생한다") {
                    shouldThrow<Exception> {
                        authenticationTokenParser.parseAccessToken(expiredAccessToken)
                    }
                }
            }
        }
        
        Given("잘못된 subject를 가진 토큰이 있을 때") {
            val userId = 1L
            val invalidToken = Jwts.builder()
                .subject("invalidSubject")
                .claim("uid", userId)
                .issuedAt(Date())
                .expiration(Date(Date().time + AuthenticationTokenGenerator.ACCESS_TOKEN_VALID_TIME))
                .signWith(secretKey)
                .compact()
            
            When("잘못된 토큰을 파싱하면") {
                Then("인증 오류 예외가 발생한다") {
                    shouldThrow<CoreException> {
                        authenticationTokenParser.parseAccessToken(invalidToken)
                    }.error shouldBe Error.UNAUTHORIZED_TOKEN
                }
            }
        }
    }
}