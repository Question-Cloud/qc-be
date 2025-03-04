package com.eager.questioncloud.core.domain.user;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.enums.UserStatus;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.model.UserInformation;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.core.exception.NotVerificationUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    @DisplayName("비밀번호를 검사하고 일치하면 예외를 발생시키지 않는다.")
    void 비밀번호를_검사하고_일치하면_예외를_발생시키지_않는다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";
        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Active);

        //when then
        assertThatCode(() -> user.passwordAuthentication(password))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않다면 로그인 실패 예외를 던진다.")
    void 비밀번호가_일치하지_않다면_로그인_실패_예외를_던진다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";
        String wrongPassword = "qwer1235";
        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Active);

        //when then
        assertThatThrownBy(() -> user.passwordAuthentication(wrongPassword))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN);
    }

    @Test
    @DisplayName("이메일 미인증 유저라면 PendingEmailVerification 예외를 던진다.")
    void 이메일_미인증_유저라면_PendingEmailVerification_예외를_던진다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";
        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.PendingEmailVerification);

        //when then
        assertThatThrownBy(user::checkUserStatus)
            .isInstanceOf(NotVerificationUserException.class);
    }

    @Test
    @DisplayName("정지 된 유저라면 NOT_ACTIVE_USER 예외를 던진다.")
    void 정지_된_유저라면_NOT_ACTIVE_USER_예외를_던진다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";
        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Ban);

        //when then
        assertThatThrownBy(user::checkUserStatus)
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ACTIVE_USER);
    }

    @Test
    @DisplayName("탈퇴 유저라면 NOT_ACTIVE_USER 예외를 던진다.")
    void 탈퇴_유저라면_NOT_ACTIVE_USER_예외를_던진다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";
        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Deleted);

        //when then
        assertThatThrownBy(user::checkUserStatus)
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ACTIVE_USER);
    }
}