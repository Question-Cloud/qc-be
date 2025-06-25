package com.eager.questioncloud.user.authentication.implement

import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.social.FailSocialLoginException
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserAuthenticatorTest(
    @Autowired val userAuthenticator: UserAuthenticator,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var socialAPIManager: SocialAPIManager

    @BeforeEach
    fun setUp() {
        UserFixtureHelper.createDefaultEmailUser(userRepository)
        UserFixtureHelper.createDefaultSocialUser(userRepository)
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `이메일 계정 로그인 성공`() {
        //given
        val email = UserFixtureHelper.defaultEmailUserEmail
        val password = UserFixtureHelper.defaultEmailUserPassword

        //when
        val result = userAuthenticator.emailPasswordAuthentication(email, password)

        //then
        Assertions.assertThat(result.uid).isNotNull()
        Assertions.assertThat(result.userInformation.email).isEqualTo(email)
    }

    @Test
    fun `존재하지 않는 이메일인 경우 로그인 실패`() {
        //given
        val wrongEmail = "wrong@email.com"
        val password = "qwer1234"

        //when then
        Assertions.assertThatThrownBy {
            userAuthenticator.emailPasswordAuthentication(wrongEmail, password)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN)
    }

    @Test
    fun `비밀번호가 일치하지 않는 경우 로그인 실패`() {
        //given
        val email = UserFixtureHelper.defaultEmailUserEmail
        val wrongPassword = "wrongPassword"

        //when //then
        Assertions.assertThatThrownBy {
            userAuthenticator.emailPasswordAuthentication(email, wrongPassword)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN)
    }

    @Test
    fun `활성화 된 계정이 아니라면 로그인 실패`() {
        //given
        val email = "inactive@test.com"
        val password = "qwer1234"
        UserFixtureHelper.createEmailUser(email, password, UserStatus.Deleted, userRepository)

        //when //then
        Assertions.assertThatThrownBy {
            userAuthenticator.emailPasswordAuthentication(email, password)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ACTIVE_USER)
    }

    @Test
    fun `이메일 인증 대기 중인 계정은 로그인 실패`() {
        //given
        val email = "pending@test.com"
        val password = "qwer1234"
        UserFixtureHelper.createEmailUser(email, password, UserStatus.PendingEmailVerification, userRepository)

        //when //then
        Assertions.assertThatThrownBy {
            userAuthenticator.emailPasswordAuthentication(email, password)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.PENDING_EMAIL_VERIFICATION)
    }

    @Test
    fun `등록되어 있는 소셜 계정 로그인 성공`() {
        //given
        val code = "socialAuthenticationCode"
        val socialAccessToken = "socialAccessToken"
        val socialUid = UserFixtureHelper.defaultSocialUserSocialUid
        val accountType = UserFixtureHelper.defaultSocialUserAccountType

        given(socialAPIManager.getAccessToken(any(), any())).willReturn(socialAccessToken)
        given(socialAPIManager.getSocialUid(any(), any())).willReturn(socialUid)

        //when
        val user = userAuthenticator.socialAuthentication(code, accountType)

        //then
        Assertions.assertThat(user).isNotNull()
        Assertions.assertThat(user.uid).isNotNull()
        Assertions.assertThat(user.userAccountInformation.accountType).isEqualTo(accountType)
    }

    @Test
    fun `등록되지 않은 소셜 계정은 로그인 실패`() {
        //given
        val code = "socialAuthenticationCode"
        val socialAccessToken = "socialAccessToken"
        val unregisteredSocialUid = "unregisteredSocialUid"
        val accountType = AccountType.KAKAO

        given(socialAPIManager.getAccessToken(any(), any())).willReturn(socialAccessToken)
        given(socialAPIManager.getSocialUid(any(), any())).willReturn(unregisteredSocialUid)

        //when then
        Assertions.assertThatThrownBy {
            userAuthenticator.socialAuthentication(code, accountType)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_REGISTERED_SOCIAL_USER)
    }

    @Test
    fun `소셜 API 호출 실패 시 로그인 실패`() {
        //given
        val wrongCode = "invalidSocialCode"
        val accountType = AccountType.KAKAO
        given(socialAPIManager.getAccessToken(any(), any())).willThrow(FailSocialLoginException())

        //when then
        Assertions.assertThatThrownBy {
            userAuthenticator.socialAuthentication(wrongCode, accountType)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_SOCIAL_LOGIN)
    }
}
