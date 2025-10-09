package com.eager.questioncloud.user.register.service

import com.eager.questioncloud.common.mail.EmailSender
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.implement.EmailVerificationProcessor
import com.eager.questioncloud.user.register.implement.UserRegister
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.EmailVerificationScenario
import com.eager.questioncloud.user.scenario.UserScenario
import com.eager.questioncloud.user.scenario.custom
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*

class RegisterUserServiceTest : BehaviorSpec() {
    private val emailVerificationProcessor = mockk<EmailVerificationProcessor>()
    private val userRepository = mockk<UserRepository>()
    private val emailSender = mockk<EmailSender>()
    private val userRegister = mockk<UserRegister>()
    
    private val registerUserService = RegisterUserService(
        emailVerificationProcessor,
        userRepository,
        emailSender,
        userRegister
    )
    
    init {
        afterEach {
            clearMocks(emailVerificationProcessor, userRepository, emailSender, userRegister)
        }
        
        Given("이메일 계정 사용자 생성 요청이 주어졌을 때") {
            val createUser = CreateUser(
                email = "test@example.com",
                password = "password123",
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = "010-1234-5678",
                name = "테스트 사용자"
            )
            val createdUser =
                UserScenario.createEmailUser("password123").custom { set(User::userStatus, UserStatus.PendingEmailVerification) }
            
            every { userRegister.create(createUser) } returns createdUser
            
            When("사용자를 생성하면") {
                val result = registerUserService.create(createUser)
                
                Then("사용자가 생성된다") {
                    result.uid shouldBe createdUser.uid
                    result.userInformation.email shouldBe createdUser.userInformation.email
                    result.userInformation.name shouldBe createdUser.userInformation.name
                    result.userStatus shouldBe UserStatus.PendingEmailVerification
                    
                    verify(exactly = 1) { userRegister.create(createUser) }
                }
            }
        }
        
        Given("소셜 계정 사용자 생성 요청이 주어졌을 때") {
            val createUser = CreateUser(
                email = "kakao@example.com",
                password = null,
                socialRegisterToken = "mock_kakao_token",
                accountType = AccountType.KAKAO,
                phone = "010-1234-5678",
                name = "카카오 사용자"
            )
            val createdUser = UserScenario.createSocialUser().custom { set(User::userStatus, UserStatus.PendingEmailVerification) }
            
            every { userRegister.create(createUser) } returns createdUser
            
            When("사용자를 생성하면") {
                val result = registerUserService.create(createUser)
                
                Then("소셜 계정 사용자가 생성된다") {
                    result.uid shouldBe createdUser.uid
                    result.userInformation.email shouldBe createdUser.userInformation.email
                    result.userAccountInformation.accountType shouldBe AccountType.KAKAO
                    result.userAccountInformation.isSocialAccount shouldBe true
                    
                    verify(exactly = 1) { userRegister.create(createUser) }
                }
            }
        }
        
        Given("사용자가 생성되었을 때") {
            val user = UserScenario.createEmailUser("password123").custom { set(User::userStatus, UserStatus.PendingEmailVerification) }
            val emailVerification = EmailVerificationScenario.create(user, EmailVerificationType.CreateUser)
            
            every {
                emailVerificationProcessor.createEmailVerification(
                    user.uid,
                    user.userInformation.email,
                    EmailVerificationType.CreateUser
                )
            } returns emailVerification
            justRun { emailSender.send(any()) }
            
            When("이메일 인증 메일을 발송하면") {
                val result = registerUserService.sendCreateUserVerifyMail(user)
                
                Then("이메일 인증 정보가 생성되고 메일이 발송된다") {
                    result.uid shouldBe user.uid
                    result.email shouldBe user.userInformation.email
                    result.emailVerificationType shouldBe EmailVerificationType.CreateUser
                    result.token shouldNotBe null
                    result.resendToken shouldNotBe null
                    
                    verify(exactly = 1) {
                        emailVerificationProcessor.createEmailVerification(
                            user.uid,
                            user.userInformation.email,
                            EmailVerificationType.CreateUser
                        )
                    }
                    verify(exactly = 1) { emailSender.send(any()) }
                }
            }
        }
        
        Given("이메일 인증 정보가 존재할 때") {
            val user = UserScenario.createEmailUser("password123").custom { set(User::userStatus, UserStatus.PendingEmailVerification) }
            val emailVerification = EmailVerificationScenario.create(user, EmailVerificationType.CreateUser)
            val resendToken = emailVerification.resendToken
            
            every { emailVerificationProcessor.getByResendToken(resendToken) } returns emailVerification
            justRun { emailSender.send(any()) }
            
            When("resend 토큰으로 이메일 인증 메일을 재발송하면") {
                registerUserService.resend(resendToken)
                
                Then("메일이 재발송된다") {
                    verify(exactly = 1) { emailVerificationProcessor.getByResendToken(resendToken) }
                    verify(exactly = 1) { emailSender.send(any()) }
                }
            }
        }
        
        Given("이메일 인증 대기 중인 사용자가 존재할 때") {
            val user = UserScenario.createEmailUser("password123").custom { set(User::userStatus, UserStatus.PendingEmailVerification) }
            user.userStatus shouldBe UserStatus.PendingEmailVerification
            
            val emailVerification = EmailVerificationScenario.create(user, EmailVerificationType.CreateUser)
            
            every {
                emailVerificationProcessor.verifyEmailVerification(
                    emailVerification.token,
                    EmailVerificationType.CreateUser
                )
            } returns emailVerification
            every { userRepository.getUser(user.uid) } returns user
            every { userRepository.save(user) } returns user
            
            When("이메일 인증을 완료하면") {
                registerUserService.verifyCreateUser(emailVerification.token, EmailVerificationType.CreateUser)
                
                Then("사용자가 활성화된다") {
                    user.userStatus shouldBe UserStatus.Active
                    
                    verify(exactly = 1) {
                        emailVerificationProcessor.verifyEmailVerification(
                            emailVerification.token,
                            EmailVerificationType.CreateUser
                        )
                    }
                    verify(exactly = 1) { userRepository.getUser(user.uid) }
                    verify(exactly = 1) { userRepository.save(user) }
                }
            }
        }
        
        Given("회원가입 전체 플로우 테스트 - 사용자 생성 요청이 주어졌을 때") {
            val password = "password123"
            val createdUser =
                UserScenario.createEmailUser(password).custom { set(User::userStatus, UserStatus.PendingEmailVerification) }
            val emailVerification = EmailVerificationScenario.create(createdUser, EmailVerificationType.CreateUser)
            
            val createUser = CreateUser(
                email = createdUser.userInformation.email,
                password = password,
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = createdUser.userInformation.phone,
                name = createdUser.userInformation.name,
            )
            
            every { userRegister.create(createUser) } returns createdUser
            every {
                emailVerificationProcessor.createEmailVerification(
                    createdUser.uid,
                    createdUser.userInformation.email,
                    EmailVerificationType.CreateUser
                )
            } returns emailVerification
            justRun { emailSender.send(any()) }
            every {
                emailVerificationProcessor.verifyEmailVerification(
                    emailVerification.token,
                    EmailVerificationType.CreateUser
                )
            } returns emailVerification
            every { userRepository.getUser(createdUser.uid) } returns createdUser
            every { userRepository.save(createdUser) } returns createdUser
            
            When("사용자를 생성하고 이메일 인증을 완료하면") {
                val user = registerUserService.create(createUser)
                user.userStatus shouldBe UserStatus.PendingEmailVerification
                
                val verification = registerUserService.sendCreateUserVerifyMail(user)
                verification.uid shouldBe user.uid
                
                registerUserService.verifyCreateUser(verification.token, EmailVerificationType.CreateUser)
                
                Then("사용자가 활성화된다") {
                    user.userStatus shouldBe UserStatus.Active
                    user.userInformation.email shouldBe createUser.email
                    user.userInformation.name shouldBe createUser.name
                    
                    verify(exactly = 1) { userRegister.create(createUser) }
                    verify(exactly = 1) { emailVerificationProcessor.createEmailVerification(any(), any(), any()) }
                    verify(exactly = 1) { emailSender.send(any()) }
                    verify(exactly = 1) { emailVerificationProcessor.verifyEmailVerification(any(), any()) }
                    verify(exactly = 1) { userRepository.getUser(any()) }
                    verify(exactly = 1) { userRepository.save(any()) }
                }
            }
        }
    }
}