package com.eager.questioncloud.user.register.implement

import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserRegistrationValidatorTest(
    @Autowired val userRegistrationValidator: UserRegistrationValidator,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `중복되지 않은 이메일 계정 정보는 검증을 통과한다`() {
        //given
        val userAccountInformation = UserAccountInformation.createEmailAccountInformation("password123")
        val userInformation = UserInformation(
            email = "new@example.com",
            phone = "010-1234-5678",
            name = "신규 사용자"
        )
        
        //when & then
        Assertions.assertThatCode {
            userRegistrationValidator.validate(userAccountInformation, userInformation)
        }.doesNotThrowAnyException()
    }
    
    @Test
    fun `중복되지 않은 소셜 계정 정보는 검증을 통과한다`() {
        //given
        val userAccountInformation =
            UserAccountInformation.createSocialAccountInformation("new_kakao_uid", AccountType.KAKAO)
        val userInformation = UserInformation(
            email = "new@kakao.com",
            phone = "010-1234-5678",
            name = "신규 카카오 사용자"
        )
        
        //when & then
        Assertions.assertThatCode {
            userRegistrationValidator.validate(userAccountInformation, userInformation)
        }.doesNotThrowAnyException()
    }
    
    @Test
    fun `중복된 이메일로 검증하면 예외가 발생한다`() {
        //given
        UserFixtureHelper.createEmailUser("duplicate@example.com", "password123", UserStatus.Active, userRepository)
        
        val userAccountInformation = UserAccountInformation.createEmailAccountInformation("newpassword123")
        val userInformation = UserInformation(
            email = "duplicate@example.com",
            phone = "010-9999-8888",
            name = "중복 이메일 사용자"
        )
        
        //when & then
        val exception = assertThrows<CoreException> {
            userRegistrationValidator.validate(userAccountInformation, userInformation)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.DUPLICATE_EMAIL)
    }
    
    @Test
    fun `중복된 전화번호로 검증하면 예외가 발생한다`() {
        //given
        val duplicatePhone = "010-0000-0000"
        
        val existingUser =
            UserFixtureHelper.createEmailUser("existing@example.com", "password123", UserStatus.Active, userRepository)
        
        val userWithDuplicatePhone = UserInformation(
            email = "temp@example.com",
            phone = duplicatePhone,
            name = "임시 사용자"
        )
        val accountInfo = UserAccountInformation.createEmailAccountInformation("temp123")
        userRepository.save(
            User.create(accountInfo, userWithDuplicatePhone, UserType.NormalUser, UserStatus.Active)
        )
        
        val userAccountInformation = UserAccountInformation.createEmailAccountInformation("password123")
        val userInformation = UserInformation(
            email = "new@example.com",
            phone = duplicatePhone,
            name = "중복 전화번호 사용자"
        )
        
        //when & then
        val exception = assertThrows<CoreException> {
            userRegistrationValidator.validate(userAccountInformation, userInformation)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.DUPLICATE_PHONE)
    }
    
    @Test
    fun `중복된 소셜 UID로 검증하면 예외가 발생한다`() {
        //given
        val duplicateSocialUid = "duplicate_kakao_uid_12345"
        
        val existingUserInfo = UserInformation(
            email = "existing@kakao.com",
            phone = "010-1111-1111",
            name = "기존 카카오 사용자"
        )
        val existingAccountInfo =
            UserAccountInformation.createSocialAccountInformation(duplicateSocialUid, AccountType.KAKAO)
        userRepository.save(
            User.create(existingAccountInfo, existingUserInfo, UserType.NormalUser, UserStatus.Active)
        )
        
        val userAccountInformation =
            UserAccountInformation.createSocialAccountInformation(duplicateSocialUid, AccountType.KAKAO)
        val userInformation = UserInformation(
            email = "new@kakao.com",
            phone = "010-2222-2222",
            name = "중복 소셜 사용자"
        )
        
        //when & then
        val exception = assertThrows<CoreException> {
            userRegistrationValidator.validate(userAccountInformation, userInformation)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.DUPLICATE_SOCIAL_UID)
    }
    
    @Test
    fun `다른 소셜 플랫폼에서 같은 UID는 중복이 아니다`() {
        //given
        val sameSocialUid = "same_uid_12345"
        
        val kakaoUserInfo = UserInformation(
            email = "kakao@example.com",
            phone = "010-1111-1111",
            name = "카카오 사용자"
        )
        val kakaoAccountInfo = UserAccountInformation.createSocialAccountInformation(sameSocialUid, AccountType.KAKAO)
        userRepository.save(
            User.create(kakaoAccountInfo, kakaoUserInfo, UserType.NormalUser, UserStatus.Active)
        )
        
        val userAccountInformation =
            UserAccountInformation.createSocialAccountInformation(sameSocialUid, AccountType.GOOGLE)
        val userInformation = UserInformation(
            email = "google@example.com",
            phone = "010-2222-2222",
            name = "구글 사용자"
        )
        
        //when & then
        Assertions.assertThatCode {
            userRegistrationValidator.validate(userAccountInformation, userInformation)
        }.doesNotThrowAnyException()
    }
}
