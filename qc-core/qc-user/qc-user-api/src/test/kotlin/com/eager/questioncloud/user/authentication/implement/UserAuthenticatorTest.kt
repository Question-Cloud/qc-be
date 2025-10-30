package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.social.FailSocialLoginException
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.UserScenario
import com.eager.questioncloud.user.scenario.custom
import com.eager.questioncloud.test.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserAuthenticatorTest(
    private val userAuthenticator: UserAuthenticator,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var socialAPIManager: SocialAPIManager
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("올바른 이메일과 비밀번호가 주어졌을 때") {
            val password = "password"
            val user = userRepository.save(UserScenario.createEmailUser(password))
            val email = user.userInformation.email
            
            
            When("이메일 계정으로 로그인하면") {
                val result = userAuthenticator.emailPasswordAuthentication(email, password)
                
                Then("로그인에 성공한다") {
                    result.uid shouldNotBe null
                    result.userInformation.email shouldBe email
                }
            }
        }
        
        Given("존재하지 않는 이메일일 때") {
            val wrongEmail = "wrong@email.com"
            val password = "qwer1234"
            
            When("로그인을 시도하면") {
                Then("로그인 실패 예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAuthenticator.emailPasswordAuthentication(wrongEmail, password)
                    }.error shouldBe Error.FAIL_LOGIN
                }
            }
        }
        
        Given("비밀번호가 일치하지 않을 때") {
            val password = "password"
            val user = userRepository.save(UserScenario.createEmailUser(password))
            val email = user.userInformation.email
            val wrongPassword = "wrongPassword"
            
            When("로그인을 시도하면") {
                Then("로그인 실패 예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAuthenticator.emailPasswordAuthentication(email, wrongPassword)
                    }.error shouldBe Error.FAIL_LOGIN
                }
            }
        }
        
        Given("활성화되지 않은 계정이 있을 때") {
            val password = "password"
            val user = userRepository.save(
                UserScenario.createEmailUser(password).custom { set(User::userStatus, UserStatus.Ban) })
            val email = user.userInformation.email
            
            When("로그인을 시도하면") {
                Then("활성 사용자가 아니라는 예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAuthenticator.emailPasswordAuthentication(email, password)
                    }.error shouldBe Error.NOT_ACTIVE_USER
                }
            }
        }
        
        Given("이메일 인증 대기 중인 계정이 있을 때") {
            val password = "password"
            val user = userRepository.save(
                UserScenario.createEmailUser(password).custom { set(User::userStatus, UserStatus.PendingEmailVerification) })
            val email = user.userInformation.email
            
            When("로그인을 시도하면") {
                Then("이메일 인증 대기 예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAuthenticator.emailPasswordAuthentication(email, password)
                    }.error shouldBe Error.PENDING_EMAIL_VERIFICATION
                }
            }
        }
        
        Given("등록되어 있는 소셜 계정이 있을 때") {
            val code = "socialAuthenticationCode"
            val socialAccessToken = "socialAccessToken"
            val user = userRepository.save(UserScenario.createSocialUser())
            val socialUid = user.userAccountInformation.socialUid!!
            val accountType = user.userAccountInformation.accountType
            
            every { socialAPIManager.getAccessToken(any(), any()) } returns socialAccessToken
            every { socialAPIManager.getSocialUid(any(), any()) } returns socialUid
            
            When("소셜 인증을 시도하면") {
                val loginUser = userAuthenticator.socialAuthentication(code, accountType)
                
                Then("로그인에 성공한다") {
                    loginUser shouldNotBe null
                    loginUser.uid shouldNotBe null
                    loginUser.userAccountInformation.accountType shouldBe accountType
                }
            }
        }
        
        Given("등록되지 않은 소셜 계정일 때") {
            val code = "socialAuthenticationCode"
            val socialAccessToken = "socialAccessToken"
            val unregisteredSocialUid = "unregisteredSocialUid"
            val accountType = AccountType.KAKAO
            
            every { socialAPIManager.getAccessToken(any(), any()) } returns socialAccessToken
            every { socialAPIManager.getSocialUid(any(), any()) } returns unregisteredSocialUid
            
            When("소셜 인증을 시도하면") {
                Then("미등록 소셜 사용자 예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAuthenticator.socialAuthentication(code, accountType)
                    }.error shouldBe Error.NOT_REGISTERED_SOCIAL_USER
                }
            }
        }
        
        Given("소셜 API 호출이 실패할 때") {
            val wrongCode = "invalidSocialCode"
            val accountType = AccountType.KAKAO
            
            every { socialAPIManager.getAccessToken(any(), any()) } throws FailSocialLoginException()
            
            When("소셜 인증을 시도하면") {
                Then("소셜 로그인 실패 예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAuthenticator.socialAuthentication(wrongCode, accountType)
                    }.error shouldBe Error.FAIL_SOCIAL_LOGIN
                }
            }
        }
    }
}