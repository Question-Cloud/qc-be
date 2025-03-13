package com.eager.questioncloud.application.api.authentication.implement

import com.eager.questioncloud.application.api.authentication.service.AuthenticationService
import com.eager.questioncloud.core.domain.user.dto.CreateUser
import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User.Companion.create
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createEmailAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createSocialAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserInformation.Companion.create
import com.eager.questioncloud.social.SocialAPIManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
internal class AuthenticationServiceTest {
    @Autowired
    private val authenticationService: AuthenticationService? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @MockBean
    private val socialAPIManager: SocialAPIManager? = null

    @Test
    @DisplayName("이메일 유저 인증에 성공하면 AuthenticationToken을 발급한다.")
    fun 이메일_유저_인증에_성공하면_AuthenticationToken을_발급한다() {
        //given
        val email = "test@test.com"
        val password = "qwer1234"

        val createUser = CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환")
        val userAccountInformation = createEmailAccountInformation(password)
        val userInformation = create(createUser)
        val user = userRepository!!.save(
            create(
                userAccountInformation,
                userInformation,
                UserType.NormalUser,
                UserStatus.Active
            )
        )

        //when
        val authenticationToken = authenticationService!!.login(email, password)

        //then
        Assertions.assertThat(authenticationToken).isNotNull()
    }

    @Test
    @DisplayName("등록된 소셜 계정이라면 AuthenticationToken을 발급한다.")
    fun 등록된_소셜_계정이라면_AuthenticationToken을_발급한다() {
        //given
        val email = "test@test.com"
        val code = "socialCode"
        val accountType = AccountType.KAKAO
        val socialAccessToken = "socialAccessToken"
        val socialUid = "socialUid"

        BDDMockito.given(socialAPIManager!!.getAccessToken(any(), any()))
            .willReturn(socialAccessToken)
        BDDMockito.given(socialAPIManager.getSocialUid(any(), any()))
            .willReturn(socialUid)

        val createUser = CreateUser(email, null, null, accountType, "01012345678", "김승환")
        val userAccountInformation = createSocialAccountInformation(socialUid, accountType)
        val userInformation = create(createUser)
        val savedUser = userRepository!!.save(
            create(
                userAccountInformation,
                userInformation,
                UserType.NormalUser,
                UserStatus.Active
            )
        )

        //when
        val socialAuthenticationResult = authenticationService!!.socialLogin(accountType, code)

        //then
        Assertions.assertThat(socialAuthenticationResult.isRegistered).isTrue()
        Assertions.assertThat(socialAuthenticationResult.authenticationToken).isNotNull()
        Assertions.assertThat(socialAuthenticationResult.registerToken).isNull()
    }

    @Test
    @DisplayName("등록되지 않은 소셜 계정이라면 RegisterToken을 발급한다.")
    fun 등록되지_않은_소셜_계정이라면_RegisterToken을_발급한다() {
        //given
        val code = "socialCode"
        val accountType = AccountType.KAKAO
        val socialAccessToken = "socialAccessToken"
        val socialUid = "socialUid"

        BDDMockito.given(socialAPIManager!!.getAccessToken(any(), any()))
            .willReturn(socialAccessToken)
        BDDMockito.given(socialAPIManager.getSocialUid(any(), any()))
            .willReturn(socialUid)

        //when
        val socialAuthenticationResult = authenticationService!!.socialLogin(accountType, code)

        //then
        Assertions.assertThat(socialAuthenticationResult.isRegistered).isFalse()

        Assertions.assertThat(socialAuthenticationResult.registerToken)
            .isNotNull()
            .isEqualTo(socialAccessToken)
    }
}