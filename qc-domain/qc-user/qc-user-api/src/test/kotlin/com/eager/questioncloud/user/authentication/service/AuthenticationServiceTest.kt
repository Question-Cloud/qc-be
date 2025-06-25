package com.eager.questioncloud.user.authentication.service

import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.social.SocialAPIManager
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
class AuthenticationServiceTest(
    @Autowired val authenticationService: AuthenticationService,
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
    fun `이메일 계정 로그인을 할 수 있다`() {
        //given
        val email = UserFixtureHelper.defaultEmailUserEmail
        val password = UserFixtureHelper.defaultEmailUserPassword

        //when
        val authenticationToken = authenticationService.login(email, password)

        //then
        Assertions.assertThat(authenticationToken).isNotNull()
        Assertions.assertThat(authenticationToken.accessToken).isNotBlank()
        Assertions.assertThat(authenticationToken.refreshToken).isNotBlank()
    }

    @Test
    fun `등록된 소셜 계정으로 로그인할 수 있다`() {
        //given
        val accountType = UserFixtureHelper.defaultSocialUserAccountType
        val socialUid = UserFixtureHelper.defaultSocialUserSocialUid
        val code = "socialAuthenticationCode"
        val socialAccessToken = "socialAccessToken"

        given(socialAPIManager.getAccessToken(any(), any())).willReturn(socialAccessToken)
        given(socialAPIManager.getSocialUid(any(), any())).willReturn(socialUid)

        //when
        val authenticationToken = authenticationService.socialLogin(accountType, code)

        //then
        Assertions.assertThat(authenticationToken).isNotNull()
        Assertions.assertThat(authenticationToken.accessToken).isNotBlank()
        Assertions.assertThat(authenticationToken.refreshToken).isNotBlank()
    }

    @Test
    fun `토큰을 갱신할 수 있다`() {
        //given
        val email = UserFixtureHelper.defaultEmailUserEmail
        val password = UserFixtureHelper.defaultEmailUserPassword
        val originalToken = authenticationService.login(email, password)

        //when
        val refreshedToken = authenticationService.refresh(originalToken.refreshToken)

        //then
        Assertions.assertThat(refreshedToken).isNotNull()
        Assertions.assertThat(refreshedToken.accessToken).isNotBlank()
        Assertions.assertThat(refreshedToken.refreshToken).isNotBlank()
    }
}
