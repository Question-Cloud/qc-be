package com.eager.questioncloud.user.authentication.service

import com.eager.questioncloud.user.authentication.implement.AuthenticationTokenGenerator
import com.eager.questioncloud.user.authentication.implement.AuthenticationTokenRefresher
import com.eager.questioncloud.user.authentication.implement.UserAuthenticator
import com.eager.questioncloud.user.authentication.model.AuthenticationToken
import com.eager.questioncloud.user.scenario.UserScenario
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class AuthenticationServiceTest : BehaviorSpec() {
    private val userAuthenticator = mockk<UserAuthenticator>()
    private val authenticationTokenGenerator = mockk<AuthenticationTokenGenerator>()
    private val authenticationTokenRefresher = mockk<AuthenticationTokenRefresher>()
    
    private val authenticationService = AuthenticationService(
        userAuthenticator,
        authenticationTokenGenerator,
        authenticationTokenRefresher
    )
    
    init {
        Given("올바른 이메일과 비밀번호가 주어졌을 때") {
            val password = "password123"
            val user = UserScenario.createEmailUser(password)
            val email = user.userInformation.email
            
            val expectedToken = AuthenticationToken(
                accessToken = "mock-access-token",
                refreshToken = "mock-refresh-token"
            )
            
            every { userAuthenticator.emailPasswordAuthentication(email, password) } returns user
            every { authenticationTokenGenerator.createAuthenticationToken(user.uid) } returns expectedToken
            
            When("이메일 계정으로 로그인하면") {
                val result = authenticationService.login(email, password)
                
                Then("인증 토큰이 발급된다") {
                    result shouldBe expectedToken
                    
                    verify(exactly = 1) { userAuthenticator.emailPasswordAuthentication(email, password) }
                    verify(exactly = 1) { authenticationTokenGenerator.createAuthenticationToken(user.uid) }
                }
            }
        }
        
        Given("소셜 로그인 코드가 주어졌을 때") {
            val code = "socialAuthCode"
            val user = UserScenario.createSocialUser()
            val accountType = user.userAccountInformation.accountType
            val expectedToken = AuthenticationToken(
                accessToken = "mock-social-access-token",
                refreshToken = "mock-social-refresh-token"
            )
            
            every { userAuthenticator.socialAuthentication(code, accountType) } returns user
            every { authenticationTokenGenerator.createAuthenticationToken(user.uid) } returns expectedToken
            
            When("소셜 로그인을 하면") {
                val result = authenticationService.socialLogin(accountType, code)
                
                Then("인증 토큰이 발급된다") {
                    result shouldBe expectedToken
                    
                    verify(exactly = 1) { userAuthenticator.socialAuthentication(code, accountType) }
                    verify(exactly = 1) { authenticationTokenGenerator.createAuthenticationToken(user.uid) }
                }
            }
        }
        
        Given("리프레시 토큰이 주어졌을 때") {
            val refreshToken = "valid-refresh-token"
            val expectedToken = AuthenticationToken(
                accessToken = "new-access-token",
                refreshToken = "new-refresh-token"
            )
            
            every { authenticationTokenRefresher.refresh(refreshToken) } returns expectedToken
            
            When("토큰을 갱신하면") {
                val result = authenticationService.refresh(refreshToken)
                
                Then("새로운 인증 토큰이 발급된다") {
                    result shouldBe expectedToken
                    
                    verify(exactly = 1) { authenticationTokenRefresher.refresh(refreshToken) }
                }
            }
        }
    }
}
