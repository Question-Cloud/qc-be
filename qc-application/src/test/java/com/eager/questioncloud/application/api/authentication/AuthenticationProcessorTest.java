package com.eager.questioncloud.application.api.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eager.questioncloud.core.domain.user.AccountType;
import com.eager.questioncloud.core.domain.user.CreateUser;
import com.eager.questioncloud.core.domain.user.User;
import com.eager.questioncloud.core.domain.user.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.UserInformation;
import com.eager.questioncloud.core.domain.user.UserRepository;
import com.eager.questioncloud.core.domain.user.UserStatus;
import com.eager.questioncloud.core.domain.user.UserType;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationProcessorTest {
    @Autowired
    private AuthenticationProcessor authenticationProcessor;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일과 비밀번호가 일치하고 Active 유저라면 유저 인증에 성공한다.")
    @Transactional
    void 이메일과_비밀번호가_일치하고_Active_유저라면_유저_인증에_성공한다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";

        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = userRepository.save(User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Active));

        //when
        User result = authenticationProcessor.emailPasswordAuthentication(email, password);

        //then
        assertThat(result.getUid()).isEqualTo(user.getUid());
    }

    @Test
    @DisplayName("존재하지 않는 이메일이면 로그인 실패 예외를 던진다.")
    @Transactional
    void 존재하지_않는_이메일이면_로그인_실패_예외를_던진다() {
        //given
        String wrongEmail = "test@test.com";
        String password = "qwer1234";

        //when then
        assertThatThrownBy(() -> authenticationProcessor.emailPasswordAuthentication(wrongEmail, password))
            .isInstanceOf(CustomException.class)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 로그인 실패 예외를 던진다.")
    @Transactional
    void 비밀번호가_일치하지_않으면_로그인_실패_예외를_던진다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";
        String wrongEmail = "qwer1235";

        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = userRepository.save(User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Active));

        //when //then
        assertThatThrownBy(() -> authenticationProcessor.emailPasswordAuthentication(email, wrongEmail))
            .isInstanceOf(CustomException.class)
            .hasFieldOrPropertyWithValue("error", Error.FAIL_LOGIN);
    }

    @Test
    @DisplayName("이메일 로그인 시 Active 상태가 아니라면 예외를 던진다.")
    @Transactional
    void 이메일_로그인_시_Active_상태가_아니라면_예외를_던진다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";

        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = userRepository.save(User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Ban));

        //when //then
        assertThatThrownBy(() -> authenticationProcessor.emailPasswordAuthentication(email, password));
    }
}