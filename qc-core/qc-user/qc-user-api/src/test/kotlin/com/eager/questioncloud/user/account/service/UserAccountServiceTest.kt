package com.eager.questioncloud.user.account.service

import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.fixture.EmailVerificationFixtureHelper
import com.eager.questioncloud.user.infrastructure.repository.EmailVerificationRepository
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.user.mail.EmailSender
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserAccountServiceTest(
    @Autowired val userAccountService: UserAccountService,
    @Autowired val userRepository: UserRepository,
    @Autowired @SpyBean val emailVerificationRepository: EmailVerificationRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @MockBean
    lateinit var emailSender: EmailSender

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `전화번호로 계정(이메일)을 찾을 수 있다`() {
        // given
        val phoneNumber = "010-1234-5678"
        val expectedEmail = "qaws5656@naver.com"

        val user = UserFixtureHelper.createEmailUser(
            expectedEmail,
            "password123",
            UserStatus.Active,
            userRepository
        )

        user.userInformation.phone = phoneNumber
        userRepository.save(user)

        // when
        val recoveredEmail = userAccountService.recoverForgottenEmail(phoneNumber)

        // then
        Assertions.assertThat(recoveredEmail).isEqualTo(expectedEmail)
    }

    @Test
    fun `존재하지 않는 전화번호로 계정을 찾는 경우 예외가 발생한다`() {
        // given
        val nonExistentPhoneNumber = "010-9999-9999"

        // when & then
        Assertions.assertThatThrownBy {
            userAccountService.recoverForgottenEmail(nonExistentPhoneNumber)
        }.isInstanceOf(CoreException::class.java)
    }

    @Test
    fun `이메일로 비밀번호 복구 메일을 발송할 수 있다`() {
        // given
        val email = "qaws5656@naver.com"
        val user = UserFixtureHelper.createEmailUser(
            email,
            "password123",
            UserStatus.Active,
            userRepository
        )
        doNothing().whenever(emailSender).sendMail(any())

        // when
        userAccountService.sendRecoverForgottenPasswordMail(email)

        // then
        val emailVerificationCaptor = argumentCaptor<EmailVerification>()
        verify(emailVerificationRepository).save(emailVerificationCaptor.capture())

        val capturedEmailVerification = emailVerificationCaptor.firstValue
        val emailVerification =
            emailVerificationRepository.get(capturedEmailVerification.token, EmailVerificationType.ChangePassword)

        Assertions.assertThat(emailVerification).isNotNull()
    }

    @Test
    fun `존재하지 않는 이메일로 비밀번호 복구 메일 발송 시 예외가 발생한다`() {
        // given
        val nonExistentEmail = "nonexistent@example.com"

        // when & then
        Assertions.assertThatThrownBy {
            userAccountService.sendRecoverForgottenPasswordMail(nonExistentEmail)
        }.isInstanceOf(CoreException::class.java)
    }

    @Test
    fun `토큰으로 비밀번호를 변경할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "tokenchange@example.com",
            "oldPassword123",
            UserStatus.Active,
            userRepository
        )

        val emailVerification = EmailVerificationFixtureHelper.createEmailVerification(
            user.uid,
            user.userInformation.email,
            EmailVerificationType.ChangePassword,
            emailVerificationRepository
        )

        val newPassword = "newPassword456"

        // when
        userAccountService.changePassword(emailVerification.token, newPassword)

        // then
        val updatedUser = userRepository.getUser(user.uid)
        assertDoesNotThrow { updatedUser.userAccountInformation.validatePassword(newPassword) }
    }

    @Test
    fun `잘못된 토큰으로 비밀번호 변경 시 예외가 발생한다`() {
        // given
        val invalidToken = "invalid-token-123"
        val newPassword = "newPassword456"

        // when & then
        Assertions.assertThatThrownBy {
            userAccountService.changePassword(invalidToken, newPassword)
        }.isInstanceOf(CoreException::class.java)
    }
}
