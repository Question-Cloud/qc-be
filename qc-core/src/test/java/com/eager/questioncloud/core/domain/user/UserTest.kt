package com.eager.questioncloud.core.domain.user

import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createEmailAccountInformation
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.eager.questioncloud.core.exception.NotVerificationUserException
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class UserTest {
    @Test
    @DisplayName("비밀번호를 검사하고 일치하면 예외를 발생시키지 않는다.")
    fun 비밀번호를_검사하고_일치하면_예외를_발생시키지_않는다() {
        //given
        val password = "qwer1234"
        val user = Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
            .set(User::userAccountInformation, createEmailAccountInformation(password))
            .set(User::uid, 1L)
            .build()
            .sample()

        //when then
        Assertions.assertThatCode { user.passwordAuthentication(password) }
            .doesNotThrowAnyException()
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않다면 로그인 실패 예외를 던진다.")
    fun 비밀번호가_일치하지_않다면_로그인_실패_예외를_던진다() {
        //given
        val password = "qwer1234"
        val wrongPassword = "qwer1235"

        val user = Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
            .set(User::userAccountInformation, createEmailAccountInformation(password))
            .set(User::uid, 1L)
            .build()
            .sample()

        //when then
        Assertions.assertThatThrownBy { user.passwordAuthentication(wrongPassword) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN)
    }

    @Test
    @DisplayName("이메일 미인증 유저라면 PendingEmailVerification 예외를 던진다.")
    fun 이메일_미인증_유저라면_PendingEmailVerification_예외를_던진다() {
        //given
        val user = Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
            .set(User::uid, 1L)
            .set(User::userStatus, UserStatus.PendingEmailVerification)
            .build()
            .sample()

        //when then
        Assertions.assertThatThrownBy { user.checkUserStatus() }
            .isInstanceOf(NotVerificationUserException::class.java)
    }

    @Test
    @DisplayName("정지 된 유저라면 NOT_ACTIVE_USER 예외를 던진다.")
    fun 정지_된_유저라면_NOT_ACTIVE_USER_예외를_던진다() {
        //given
        val user = Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
            .set(User::uid, 1L)
            .set(User::userStatus, UserStatus.Ban)
            .build()
            .sample()

        //when then
        Assertions.assertThatThrownBy { user.checkUserStatus() }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ACTIVE_USER)
    }

    @Test
    @DisplayName("탈퇴 유저라면 NOT_ACTIVE_USER 예외를 던진다.")
    fun 탈퇴_유저라면_NOT_ACTIVE_USER_예외를_던진다() {
        //given
        val user = Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
            .set(User::uid, 1L)
            .set(User::userStatus, UserStatus.Deleted)
            .build()
            .sample()

        //when then
        Assertions.assertThatThrownBy { user.checkUserStatus() }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ACTIVE_USER)
    }
}