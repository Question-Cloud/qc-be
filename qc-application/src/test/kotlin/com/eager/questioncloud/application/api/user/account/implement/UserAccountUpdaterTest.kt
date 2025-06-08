package com.eager.questioncloud.application.api.user.account.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.implement.PasswordProcessor
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserAccountUpdaterTest(
    @Autowired val userAccountUpdater: UserAccountUpdater,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `이메일 계정 사용자의 비밀번호를 변경할 수 있다`() {
        // given
        val originalPassword = "originalPassword123"
        val newPassword = "newPassword456"

        val user = UserFixtureHelper.createEmailUser(
            "test@example.com",
            originalPassword,
            UserStatus.Active,
            userRepository
        )

        // when
        userAccountUpdater.changePassword(user.uid, newPassword)

        // then
        val updatedUser = userRepository.getUser(user.uid)

        Assertions.assertThatCode {
            updatedUser.passwordAuthentication(newPassword)
        }.doesNotThrowAnyException()

        Assertions.assertThatThrownBy {
            updatedUser.passwordAuthentication(originalPassword)
        }.isInstanceOf(CoreException::class.java)
            .hasMessage(Error.FAIL_LOGIN.message)

        Assertions.assertThat(updatedUser.userAccountInformation.password).isNotEqualTo(newPassword)
        Assertions.assertThat(PasswordProcessor.matches(newPassword, updatedUser.userAccountInformation.password!!))
            .isTrue()
    }

    @Test
    fun `소셜 계정 사용자의 비밀번호 변경 시 예외가 발생한다`() {
        // given
        val socialUser = UserFixtureHelper.createDefaultSocialUser(userRepository)

        val newPassword = "attemptedPassword123"

        // when & then
        Assertions.assertThatThrownBy {
            userAccountUpdater.changePassword(socialUser.uid, newPassword)
        }.isInstanceOf(CoreException::class.java)
            .hasMessage(Error.NOT_PASSWORD_SUPPORT_ACCOUNT.message)
    }
}
