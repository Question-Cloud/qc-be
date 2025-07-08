package com.eager.questioncloud.user.register.implement

import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.user.dto.CreateUser
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserRegisterTest(
    @Autowired val userRegister: UserRegister,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var socialAPIManager: SocialAPIManager

    @MockBean
    lateinit var pointCommandAPI: PointCommandAPI

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `이메일 계정 사용자를 생성할 수 있다`() {
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
        val createdUser = userRegister.create(createUser)

        //then
        Assertions.assertThat(createdUser.uid).isNotNull()
        Assertions.assertThat(createdUser.userInformation.email).isEqualTo("test@example.com")
        Assertions.assertThat(createdUser.userInformation.name).isEqualTo("테스트 사용자")
        Assertions.assertThat(createdUser.userInformation.phone).isEqualTo("010-1234-5678")
        Assertions.assertThat(createdUser.userType).isEqualTo(UserType.NormalUser)
        Assertions.assertThat(createdUser.userStatus).isEqualTo(UserStatus.PendingEmailVerification)
        Assertions.assertThat(createdUser.userAccountInformation.accountType).isEqualTo(AccountType.EMAIL)
        Assertions.assertThat(createdUser.userAccountInformation.isSocialAccount).isFalse()
    }

    @Test
    fun `카카오 소셜 계정으로 사용자를 생성할 수 있다`() {
        //given
        given(socialAPIManager.getSocialUid(any(), any())).willReturn("kakao_social_uid_12345")

        val createUser = CreateUser(
            email = "kakao@example.com",
            password = null,
            socialRegisterToken = "mock_kakao_token",
            accountType = AccountType.KAKAO,
            phone = "010-1234-5678",
            name = "카카오 사용자"
        )

        //when
        val createdUser = userRegister.create(createUser)

        //then
        Assertions.assertThat(createdUser.uid).isNotNull()
        Assertions.assertThat(createdUser.userInformation.email).isEqualTo("kakao@example.com")
        Assertions.assertThat(createdUser.userInformation.name).isEqualTo("카카오 사용자")
        Assertions.assertThat(createdUser.userAccountInformation.accountType).isEqualTo(AccountType.KAKAO)
        Assertions.assertThat(createdUser.userAccountInformation.isSocialAccount).isTrue()
        Assertions.assertThat(createdUser.userAccountInformation.socialUid).isEqualTo("kakao_social_uid_12345")
    }

    @Test
    fun `구글 소셜 계정으로 사용자를 생성할 수 있다`() {
        //given
        given(socialAPIManager.getSocialUid(any(), any())).willReturn("google_social_uid_67890")

        val createUser = CreateUser(
            email = "google@example.com",
            password = null,
            socialRegisterToken = "mock_google_token",
            accountType = AccountType.GOOGLE,
            phone = "010-9876-5432",
            name = "구글 사용자"
        )

        //when
        val createdUser = userRegister.create(createUser)

        //then
        Assertions.assertThat(createdUser.uid).isNotNull()
        Assertions.assertThat(createdUser.userInformation.email).isEqualTo("google@example.com")
        Assertions.assertThat(createdUser.userInformation.name).isEqualTo("구글 사용자")
        Assertions.assertThat(createdUser.userAccountInformation.accountType).isEqualTo(AccountType.GOOGLE)
        Assertions.assertThat(createdUser.userAccountInformation.isSocialAccount).isTrue()
        Assertions.assertThat(createdUser.userAccountInformation.socialUid).isEqualTo("google_social_uid_67890")
    }

    @Test
    fun `네이버 소셜 계정으로 사용자를 생성할 수 있다`() {
        //given
        given(socialAPIManager.getSocialUid(any(), any())).willReturn("naver_social_uid_11111")

        val createUser = CreateUser(
            email = "naver@example.com",
            password = null,
            socialRegisterToken = "mock_naver_token",
            accountType = AccountType.NAVER,
            phone = "010-5555-6666",
            name = "네이버 사용자"
        )

        //when
        val createdUser = userRegister.create(createUser)

        //then
        Assertions.assertThat(createdUser.uid).isNotNull()
        Assertions.assertThat(createdUser.userInformation.email).isEqualTo("naver@example.com")
        Assertions.assertThat(createdUser.userInformation.name).isEqualTo("네이버 사용자")
        Assertions.assertThat(createdUser.userAccountInformation.accountType).isEqualTo(AccountType.NAVER)
        Assertions.assertThat(createdUser.userAccountInformation.isSocialAccount).isTrue()
        Assertions.assertThat(createdUser.userAccountInformation.socialUid).isEqualTo("naver_social_uid_11111")
    }

    @Test
    fun `이미 등록된 이메일로 사용자를 생성하려고 하면 예외가 발생한다`() {
        //given
        UserFixtureHelper.createEmailUser("duplicate@example.com", "password123", UserStatus.Active, userRepository)

        val existingUser = CreateUser(
            email = "duplicate@example.com",
            password = "password456",
            socialRegisterToken = null,
            accountType = AccountType.EMAIL,
            phone = "010-1111-2222",
            name = "중복 사용자"
        )

        //when & then
        val exception = assertThrows<CoreException> {
            userRegister.create(existingUser)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.DUPLICATE_EMAIL)
    }

    @Test
    fun `이미 등록된 전화번호로 사용자를 생성하려고 하면 예외가 발생한다`() {
        //given
        val duplicatePhone = "010-0000-0000"

        val existingUser = CreateUser(
            email = "existing@example.com",
            password = "existing123",
            socialRegisterToken = null,
            accountType = AccountType.EMAIL,
            phone = duplicatePhone,
            name = "기존 사용자"
        )
        userRegister.create(existingUser)

        val createUser = CreateUser(
            email = "test2@example.com",
            password = "password456",
            socialRegisterToken = null,
            accountType = AccountType.EMAIL,
            phone = duplicatePhone,
            name = "중복 전화번호 사용자"
        )

        //when & then
        val exception = assertThrows<CoreException> {
            userRegister.create(createUser)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.DUPLICATE_PHONE)
    }

    @Test
    fun `이미 등록된 소셜 UID로 사용자를 생성하려고 하면 예외가 발생한다`() {
        //given
        val duplicateSocialUid = "duplicate_social_uid_12345"

        given(socialAPIManager.getSocialUid(any(), any())).willReturn(duplicateSocialUid)

        given(socialAPIManager.getSocialUid(any(), any())).willReturn(duplicateSocialUid)

        val existingUser = CreateUser(
            email = "existing@kakao.com",
            password = null,
            socialRegisterToken = "token1",
            accountType = AccountType.KAKAO,
            phone = "010-1111-1111",
            name = "기존 카카오 사용자"
        )
        userRegister.create(existingUser)

        val createUser = CreateUser(
            email = "new@kakao.com",
            password = null,
            socialRegisterToken = "token2",
            accountType = AccountType.KAKAO,
            phone = "010-2222-2222",
            name = "중복 소셜 사용자"
        )

        //when & then
        val exception = assertThrows<CoreException> {
            userRegister.create(createUser)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.DUPLICATE_SOCIAL_UID)
    }

    @Test
    fun `사용자 생성 시 포인트가 초기화된다`() {
        //given
        val createUser = CreateUser(
            email = "point@example.com",
            password = "password123",
            socialRegisterToken = null,
            accountType = AccountType.EMAIL,
            phone = "010-5555-6666",
            name = "포인트 테스트 사용자"
        )

        //when
        val createdUser = userRegister.create(createUser)

        //then
        Assertions.assertThat(createdUser.uid).isNotNull()
        verify(pointCommandAPI, times(1)).initialize(any())
    }
}
