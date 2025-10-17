package com.eager.questioncloud.user.account.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.mail.EmailSender
import com.eager.questioncloud.user.account.implement.UserAccountUpdater
import com.eager.questioncloud.user.common.implement.UserFinder
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.implement.EmailVerificationProcessor
import com.eager.questioncloud.user.scenario.EmailVerificationScenario
import com.eager.questioncloud.user.scenario.UserScenario
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class UserAccountServiceTest : BehaviorSpec() {
    private val userFinder = mockk<UserFinder>()
    private val emailVerificationProcessor = mockk<EmailVerificationProcessor>()
    private val userAccountUpdater = mockk<UserAccountUpdater>()
    private val emailSender = mockk<EmailSender>()
    
    private val userAccountService = UserAccountService(
        userFinder,
        emailVerificationProcessor,
        userAccountUpdater,
        emailSender
    )
    
    init {
        Given("전화번호가 등록된 사용자가 있을 때") {
            val password = "password"
            val user = UserScenario.createEmailUser(password)
            val phoneNumber = user.userInformation.phone
            val expectedEmail = user.userInformation.email
            
            every { userFinder.getByPhone(phoneNumber) } returns user
            
            When("전화번호로 계정을 찾으면") {
                val recoveredEmail = userAccountService.recoverForgottenEmail(phoneNumber)
                
                Then("이메일을 찾을 수 있다") {
                    recoveredEmail shouldBe expectedEmail
                }
            }
        }
        
        Given("존재하지 않는 전화번호일 때") {
            val nonExistentPhoneNumber = "010-9999-9999"
            
            every { userFinder.getByPhone(nonExistentPhoneNumber) } throws CoreException(Error.NOT_FOUND)
            
            When("해당 전화번호로 계정을 찾으면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAccountService.recoverForgottenEmail(nonExistentPhoneNumber)
                    }
                }
            }
        }
        
        Given("이메일 계정 사용자가 있을 때") {
            val password = "password"
            val user = UserScenario.createEmailUser(password)
            val email = user.userInformation.email
            val emailVerification = EmailVerificationScenario.create(user, EmailVerificationType.ChangePassword)
            
            every { userFinder.getByEmail(email) } returns user
            every {
                emailVerificationProcessor.createEmailVerification(
                    user.uid,
                    email,
                    EmailVerificationType.ChangePassword
                )
            } returns emailVerification
            justRun { emailSender.send(any()) }
            
            When("비밀번호 복구 메일을 발송하면") {
                userAccountService.sendRecoverForgottenPasswordMail(email)
                
                Then("이메일 인증 정보가 생성되고 메일이 발송된다") {
                    verify(exactly = 1) { userFinder.getByEmail(email) }
                    verify(exactly = 1) {
                        emailVerificationProcessor.createEmailVerification(
                            user.uid,
                            email,
                            EmailVerificationType.ChangePassword
                        )
                    }
                    verify(exactly = 1) { emailSender.send(any()) }
                }
            }
        }
        
        Given("존재하지 않는 이메일일 때") {
            val nonExistentEmail = "nonexistent@example.com"
            
            every { userFinder.getByEmail(nonExistentEmail) } throws CoreException(Error.NOT_FOUND)
            
            When("비밀번호 복구 메일을 발송하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAccountService.sendRecoverForgottenPasswordMail(nonExistentEmail)
                    }
                }
            }
        }
        
        Given("유효한 비밀번호 변경 토큰이 있을 때") {
            val password = "password"
            val user = UserScenario.createEmailUser(password)
            val emailVerification = EmailVerificationScenario.create(user, EmailVerificationType.ChangePassword)
            val newPassword = "newPassword456"
            
            every {
                emailVerificationProcessor.verifyEmailVerification(
                    emailVerification.token,
                    EmailVerificationType.ChangePassword
                )
            } returns emailVerification
            justRun { userAccountUpdater.changePassword(user.uid, newPassword) }
            
            When("토큰으로 비밀번호를 변경하면") {
                userAccountService.changePassword(emailVerification.token, newPassword)
                
                Then("비밀번호가 변경된다") {
                    verify(exactly = 1) {
                        emailVerificationProcessor.verifyEmailVerification(
                            emailVerification.token,
                            EmailVerificationType.ChangePassword
                        )
                    }
                    verify(exactly = 1) { userAccountUpdater.changePassword(user.uid, newPassword) }
                }
            }
        }
        
        Given("잘못된 토큰일 때") {
            val invalidToken = "invalid-token-123"
            val newPassword = "newPassword456"
            
            every {
                emailVerificationProcessor.verifyEmailVerification(
                    invalidToken,
                    EmailVerificationType.ChangePassword
                )
            } throws CoreException(Error.UNAUTHORIZED_TOKEN)
            
            When("토큰으로 비밀번호를 변경하면") {
                Then("예외가 발생한다") {
                    shouldThrow<CoreException> {
                        userAccountService.changePassword(invalidToken, newPassword)
                    }
                }
            }
        }
    }
}