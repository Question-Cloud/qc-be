package com.eager.questioncloud.core.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일이 일치하는 유저를 조회한다.")
    void 이메일이_일치하는_유저를_조회한다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";

        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User savedUser = userRepository.save(User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Active));

        //when
        User user = userRepository.getUserByEmail(email);

        //then
        assertThat(user.getUserInformation().getEmail()).isEqualTo(email);
    }
}