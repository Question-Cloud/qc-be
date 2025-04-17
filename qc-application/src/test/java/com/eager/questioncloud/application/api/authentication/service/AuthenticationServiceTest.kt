package com.eager.questioncloud.application.api.authentication.service

import com.eager.questioncloud.application.utils.UserFixtureHelper
import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.social.SocialAPIManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationServiceTest(
    @Autowired val authenticationService: AuthenticationService,
    @Autowired val userRepository: UserRepository,
    @Autowired @MockBean val socialAPIManager: SocialAPIManager,
) {
    @BeforeEach
    fun setUp() {
        UserFixtureHelper.createDefaultEmailUser(userRepository)
        UserFixtureHelper.createDefaultSocialUser(userRepository)
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAllInBatch()
    }

    @Test
    fun 이메일_계정_로그인을_할_수_있다() {
        //given
        val email = UserFixtureHelper.defaultEmailUserEmail
        val password = UserFixtureHelper.defaultEmailUserPassword

        //when
        val authenticationToken = authenticationService.login(email, password)

        //then
        Assertions.assertThat(authenticationToken).isNotNull()
    }

    @Test
    fun `등록된 소셜 계정 인증 성공`() {
        //given
        val accountType = UserFixtureHelper.defaultSocialUserAccountType
        val socialUid = UserFixtureHelper.defaultSocialUserSocialUid
        val code = "socialAuthenticationCode"
        val socialAccessToken = "socialAccessToken"


        BDDMockito.given(socialAPIManager.getAccessToken(any(), any()))
            .willReturn(socialAccessToken)
        BDDMockito.given(socialAPIManager.getSocialUid(any(), any()))
            .willReturn(socialUid)

        //when
        val socialAuthenticationResult = authenticationService.socialLogin(accountType, code)

        //then
        Assertions.assertThat(socialAuthenticationResult.isRegistered).isTrue()
        Assertions.assertThat(socialAuthenticationResult.authenticationToken).isNotNull()
        Assertions.assertThat(socialAuthenticationResult.registerToken).isNull()
    }

    @Test
    fun `등록되지 않은 소셜 계정 인증 성공`() {
        //given
        val code = "socialAuthenticationCode"
        val accountType = AccountType.KAKAO
        val socialAccessToken = "socialAccessToken"
        val socialUid = "unregisterdSocialUid"

        BDDMockito.given(socialAPIManager.getAccessToken(any(), any()))
            .willReturn(socialAccessToken)
        BDDMockito.given(socialAPIManager.getSocialUid(any(), any()))
            .willReturn(socialUid)

        //when
        val socialAuthenticationResult = authenticationService.socialLogin(accountType, code)

        //then
        Assertions.assertThat(socialAuthenticationResult.isRegistered).isFalse()

        Assertions.assertThat(socialAuthenticationResult.registerToken)
            .isNotNull()
            .isEqualTo(socialAccessToken)
    }
}