package com.eager.questioncloud.user.register.service

import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.social.SocialPlatform
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.enums.AccountType
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
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class RegisterUserServiceTest(
    @Autowired val registerUserService: RegisterUserService,
    @Autowired val userRepository: UserRepository,
    @Autowired val emailVerificationRepository: EmailVerificationRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @MockBean
    lateinit var emailSender: EmailSender

    @MockBean
    lateinit var socialAPIManager: SocialAPIManager

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `이메일 계정으로 사용자를 생성할 수 있다`() {
        //given
        val createUser = CreateUser(
            email = "test@example.com",
            password = "password123",
            socialRegisterToken = null,
            accountType = AccountType.EMAIL,
            phone = "010-1234-5678",
            name = "테스트 사용자"
        )

        //when
        val createdUser = registerUserService.create(createUser)

        //then
        Assertions.assertThat(createdUser.uid).isNotNull()
        Assertions.assertThat(createdUser.userInformation.email).isEqualTo("test@example.com")
        Assertions.assertThat(createdUser.userInformation.name).isEqualTo("테스트 사용자")
        Assertions.assertThat(createdUser.userStatus).isEqualTo(UserStatus.PendingEmailVerification)
    }

    @Test
    fun `소셜 계정으로 사용자를 생성할 수 있다`() {
        //given
        whenever(socialAPIManager.getSocialUid("mock_kakao_token", SocialPlatform.KAKAO))
            .thenReturn("kakao_social_uid_12345")

        val createUser = CreateUser(
            email = "kakao@example.com",
            password = null,
            socialRegisterToken = "mock_kakao_token",
            accountType = AccountType.KAKAO,
            phone = "010-1234-5678",
            name = "카카오 사용자"
        )

        //when
        val createdUser = registerUserService.create(createUser)

        //then
        Assertions.assertThat(createdUser.uid).isNotNull()
        Assertions.assertThat(createdUser.userInformation.email).isEqualTo("kakao@example.com")
        Assertions.assertThat(createdUser.userAccountInformation.accountType).isEqualTo(AccountType.KAKAO)
        Assertions.assertThat(createdUser.userAccountInformation.socialUid).isEqualTo("kakao_social_uid_12345")
    }

    @Test
    fun `사용자 생성 후 이메일 인증 메일을 발송할 수 있다`() {
        //given
        val user = UserFixtureHelper.createEmailUser(
            "qaws5656@naver.com",
            "password123",
            UserStatus.PendingEmailVerification,
            userRepository
        )

        doNothing().whenever(emailSender).sendMail(any())

        //when
        val emailVerification = registerUserService.sendCreateUserVerifyMail(user)

        //then
        Assertions.assertThat(emailVerification.uid).isEqualTo(user.uid)
        Assertions.assertThat(emailVerification.email).isEqualTo(user.userInformation.email)
        Assertions.assertThat(emailVerification.emailVerificationType).isEqualTo(EmailVerificationType.CreateUser)
        Assertions.assertThat(emailVerification.token).isNotBlank()
        Assertions.assertThat(emailVerification.resendToken).isNotBlank()

        val savedEmailVerification = emailVerificationRepository.getCreateUserVerification(user.uid)
        Assertions.assertThat(savedEmailVerification).isNotNull()
        Assertions.assertThat(savedEmailVerification.uid).isEqualTo(user.uid)
    }

    @Test
    fun `resend 토큰으로 이메일 인증 메일을 재발송할 수 있다`() {
        //given
        val user = UserFixtureHelper.createEmailUser(
            "user@example.com",
            "password123",
            UserStatus.PendingEmailVerification,
            userRepository
        )
        val emailVerification = EmailVerificationFixtureHelper.createEmailVerification(
            user.uid,
            user.userInformation.email,
            EmailVerificationType.CreateUser,
            emailVerificationRepository
        )

        doNothing().whenever(emailSender).sendMail(any())

        //when & then
        Assertions.assertThatCode {
            registerUserService.resend(emailVerification.resendToken)
        }.doesNotThrowAnyException()
    }

    @Test
    fun `이메일 인증을 완료하면 사용자가 활성화된다`() {
        //given
        val user = UserFixtureHelper.createEmailUser(
            "user@example.com",
            "password123",
            UserStatus.PendingEmailVerification,
            userRepository
        )
        val emailVerification = EmailVerificationFixtureHelper.createEmailVerification(
            user.uid,
            user.userInformation.email,
            EmailVerificationType.CreateUser,
            emailVerificationRepository
        )

        val userBeforeVerification = userRepository.getUser(user.uid)
        Assertions.assertThat(userBeforeVerification.userStatus).isEqualTo(UserStatus.PendingEmailVerification)

        //when
        registerUserService.verifyCreateUser(emailVerification.token, EmailVerificationType.CreateUser)

        //then
        val userAfterVerification = userRepository.getUser(user.uid)
        Assertions.assertThat(userAfterVerification.userStatus).isEqualTo(UserStatus.Active)
    }

    @Test
    fun `회원가입 전체 플로우가 정상 동작한다`() {
        //given
        val createUser = CreateUser(
            email = "flow@example.com",
            password = "password123",
            socialRegisterToken = null,
            accountType = AccountType.EMAIL,
            phone = "010-5555-6666",
            name = "플로우 테스트 사용자"
        )

        doNothing().whenever(emailSender).sendMail(any())

        //when
        val createdUser = registerUserService.create(createUser)
        Assertions.assertThat(createdUser.userStatus).isEqualTo(UserStatus.PendingEmailVerification)

        val emailVerification = registerUserService.sendCreateUserVerifyMail(createdUser)
        Assertions.assertThat(emailVerification.uid).isEqualTo(createdUser.uid)

        registerUserService.verifyCreateUser(emailVerification.token, EmailVerificationType.CreateUser)

        //then
        val finalUser = userRepository.getUser(createdUser.uid)
        Assertions.assertThat(finalUser.userStatus).isEqualTo(UserStatus.Active)
        Assertions.assertThat(finalUser.userInformation.email).isEqualTo("flow@example.com")
        Assertions.assertThat(finalUser.userInformation.name).isEqualTo("플로우 테스트 사용자")
    }
}
