package com.eager.questioncloud.application.api.authentication.implement

import com.eager.questioncloud.core.domain.user.dto.CreateUser
import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User.Companion.create
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createEmailAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createSocialAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserInformation.Companion.create
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.social.SocialAPIManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationProcessorTest(
    @Autowired val authenticationProcessor: AuthenticationProcessor,
    @Autowired val userRepository: UserRepository,
    @Autowired @MockBean val socialAPIManager: SocialAPIManager,
) {
    @AfterEach
    fun tearDown() {
        userRepository.deleteAllInBatch()
    }

    @Test
    fun `이메일 계정 로그인 성공`() {
        //given
        val email = "test@test.com"
        val password = "qwer1234"

        val createUser = CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환")
        val userAccountInformation = createEmailAccountInformation(password)
        val userInformation = create(createUser)
        val user = userRepository.save(
            create(
                userAccountInformation,
                userInformation,
                UserType.NormalUser,
                UserStatus.Active
            )
        )

        //when
        val result = authenticationProcessor.emailPasswordAuthentication(email, password)

        //then
        Assertions.assertThat(result.uid).isEqualTo(user.uid)
    }

    @Test
    fun `존재하지 않는 이메일인 경우 로그인 실패`() {
        //given
        val wrongEmail = "test@test.com"
        val password = "qwer1234"

        //when then
        Assertions.assertThatThrownBy {
            authenticationProcessor.emailPasswordAuthentication(
                wrongEmail,
                password
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN)
    }

    @Test
    fun `비밀번호가 일치하지 않는 경우 로그인 실패`() {
        //given
        val email = "test@test.com"
        val password = "qwer1234"
        val wrongEmail = "qwer1235"

        val createUser = CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환")
        val userAccountInformation = createEmailAccountInformation(password)
        val userInformation = create(createUser)
        val user = userRepository.save(
            create(
                userAccountInformation,
                userInformation,
                UserType.NormalUser,
                UserStatus.Active
            )
        )

        //when //then
        Assertions.assertThatThrownBy {
            authenticationProcessor.emailPasswordAuthentication(
                email,
                wrongEmail
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN)
    }

    @Test
    fun `활성화 된 계정이 아니라면 로그인 실패`() {
        //given
        val email = "test@test.com"
        val password = "qwer1234"

        val createUser = CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환")
        val userAccountInformation = createEmailAccountInformation(password)
        val userInformation = create(createUser)
        val user =
            userRepository.save(create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Ban))

        //when //then
        Assertions.assertThatThrownBy {
            authenticationProcessor.emailPasswordAuthentication(
                email,
                password
            )
        }
    }

    @Test
    fun `등록되어 있는 소셜 계정 로그인 성공`() {
        //given
        val email = "test@test.com"
        val code = "socialCode"
        val accountType = AccountType.KAKAO
        val socialAccessToken = "socialAccessToken"
        val socialUid = "socialUid"

        BDDMockito.given(socialAPIManager.getAccessToken(any(), any()))
            .willReturn(socialAccessToken)
        BDDMockito.given(socialAPIManager.getSocialUid(any(), any()))
            .willReturn(socialUid)

        val createUser = CreateUser(email, null, null, accountType, "01012345678", "김승환")
        val userAccountInformation = createSocialAccountInformation(socialUid, accountType)
        val userInformation = create(createUser)
        val savedUser = userRepository.save(
            create(
                userAccountInformation,
                userInformation,
                UserType.NormalUser,
                UserStatus.Active
            )
        )

        //when
        val socialAuthentication = authenticationProcessor.socialAuthentication(code, accountType)

        //then
        Assertions.assertThat(socialAuthentication)
            .extracting("user")
            .isNotNull()
    }

    @Test
    fun `미가입 소셜 계정이라면 socialAccessToken 반환`() {
        //given
        val email = "test@test.com"
        val code = "socialCode"
        val accountType = AccountType.KAKAO
        val socialAccessToken = "socialAccessToken"
        val socialUid = "socialUid"

        BDDMockito.given(socialAPIManager.getAccessToken(any(), any()))
            .willReturn(socialAccessToken)
        BDDMockito.given(socialAPIManager.getSocialUid(any(), any()))
            .willReturn(socialUid)

        //when
        val socialAuthentication = authenticationProcessor.socialAuthentication(code, accountType)

        //then
        Assertions.assertThat(socialAuthentication)
            .extracting("socialAccessToken")
            .isNotNull()
            .isEqualTo(socialAccessToken)

        Assertions.assertThat(socialAuthentication)
            .extracting("user")
            .isNull()
    }

    @Test
    fun `올바르지 않은 소셜 계정 로그인 실패`() {
        //given
        val wrongCode = "socialCode"
        val accountType = AccountType.KAKAO
        BDDMockito.given(socialAPIManager.getAccessToken(any(), any()))
            .willThrow(CoreException(Error.FAIL_SOCIAL_LOGIN))

        //when then
        Assertions.assertThatThrownBy {
            authenticationProcessor.socialAuthentication(
                wrongCode,
                accountType
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_SOCIAL_LOGIN)
    }
}