package com.eager.questioncloud.application.api.authentication.service

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
internal class AuthenticationServiceTest {
    @Autowired
    private val authenticationService: AuthenticationService? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @MockBean
    private val socialAPIManager: SocialAPIManager? = null

    @AfterEach
    fun tearDown() {
        userRepository!!.deleteAllInBatch()
    }

    @Test
    fun 이메일_계정_로그인을_할_수_있다() {
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