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
internal class AuthenticationProcessorTest {
    @Autowired
    private val authenticationProcessor: AuthenticationProcessor? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @MockBean
    private val socialAPIManager: SocialAPIManager? = null

    @AfterEach
    fun tearDown() {
        userRepository!!.deleteAllInBatch()
    }

    @Test
    fun 이메일과_비밀번호가_일치하고_Active_유저라면_유저_객체를_반환한다() {
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
        val result = authenticationProcessor!!.emailPasswordAuthentication(email, password)

        //then
        Assertions.assertThat(result.uid).isEqualTo(user.uid)
    }

    @Test
    fun 존재하지_않는_이메일이면_로그인_실패_예외를_던진다() {
        //given
        val wrongEmail = "test@test.com"
        val password = "qwer1234"

        //when then
        Assertions.assertThatThrownBy {
            authenticationProcessor!!.emailPasswordAuthentication(
                wrongEmail,
                password
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN)
    }

    @Test
    fun 비밀번호가_일치하지_않으면_로그인_실패_예외를_던진다() {
        //given
        val email = "test@test.com"
        val password = "qwer1234"
        val wrongEmail = "qwer1235"

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

        //when //then
        Assertions.assertThatThrownBy {
            authenticationProcessor!!.emailPasswordAuthentication(
                email,
                wrongEmail
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN)
    }

    @Test
    fun 이메일_로그인_시_Active_상태가_아니라면_예외를_던진다() {
        //given
        val email = "test@test.com"
        val password = "qwer1234"

        val createUser = CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환")
        val userAccountInformation = createEmailAccountInformation(password)
        val userInformation = create(createUser)
        val user =
            userRepository!!.save(create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Ban))

        //when //then
        Assertions.assertThatThrownBy {
            authenticationProcessor!!.emailPasswordAuthentication(
                email,
                password
            )
        }
    }

    @Test
    fun 가입된_소셜_계정이라면_User가_담긴_SocialAuthentication을_반환한다() {
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
        val socialAuthentication = authenticationProcessor!!.socialAuthentication(code, accountType)

        //then
        Assertions.assertThat(socialAuthentication)
            .extracting("user")
            .isNotNull()
    }

    @Test
    fun 미가입_소셜_계정이라면_socialAccessToken이_담긴_SocialAuthentication을_반환한다() {
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

        //when
        val socialAuthentication = authenticationProcessor!!.socialAuthentication(code, accountType)

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
    fun 올바르지_않은_social_code가_전달되면_소셜_로그인_실패_예외를_던진다() {
        //given
        val wrongCode = "socialCode"
        val accountType = AccountType.KAKAO
        BDDMockito.given(socialAPIManager!!.getAccessToken(any(), any()))
            .willThrow(CoreException(Error.FAIL_SOCIAL_LOGIN))

        //when then
        Assertions.assertThatThrownBy {
            authenticationProcessor!!.socialAuthentication(
                wrongCode,
                accountType
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_SOCIAL_LOGIN)
    }
}