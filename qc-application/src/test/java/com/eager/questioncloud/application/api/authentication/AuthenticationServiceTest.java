package com.eager.questioncloud.application.api.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.eager.questioncloud.application.api.authentication.dto.AuthenticationToken;
import com.eager.questioncloud.application.api.authentication.dto.SocialAuthenticationResult;
import com.eager.questioncloud.application.business.authentication.service.AuthenticationService;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.enums.UserStatus;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.model.UserInformation;
import com.eager.questioncloud.social.SocialAPIManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private SocialAPIManager socialAPIManager;

    @Test
    @DisplayName("이메일 유저 인증에 성공하면 AuthenticationToken을 발급한다.")
    void 이메일_유저_인증에_성공하면_AuthenticationToken을_발급한다() {
        //given
        String email = "test@test.com";
        String password = "qwer1234";

        CreateUser createUser = new CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createEmailAccountInformation(password);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = userRepository.save(User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Active));

        //when
        AuthenticationToken authenticationToken = authenticationService.login(email, password);

        //then
        assertThat(authenticationToken).isNotNull();
    }

    @Test
    @DisplayName("등록된 소셜 계정이라면 AuthenticationToken을 발급한다.")
    void 등록된_소셜_계정이라면_AuthenticationToken을_발급한다() {
        //given
        String email = "test@test.com";
        String code = "socialCode";
        AccountType accountType = AccountType.KAKAO;
        String socialAccessToken = "socialAccessToken";
        String socialUid = "socialUid";

        BDDMockito.given(socialAPIManager.getAccessToken(any(), any())).willReturn(socialAccessToken);
        BDDMockito.given(socialAPIManager.getSocialUid(any(), any())).willReturn(socialUid);

        CreateUser createUser = new CreateUser(email, null, null, accountType, "01012345678", "김승환");
        UserAccountInformation userAccountInformation = UserAccountInformation.createSocialAccountInformation(socialUid, accountType);
        UserInformation userInformation = UserInformation.create(createUser);
        User savedUser = userRepository.save(User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.Active));

        //when
        SocialAuthenticationResult socialAuthenticationResult = authenticationService.socialLogin(accountType, code);

        //then
        assertThat(socialAuthenticationResult.getIsRegistered()).isTrue();
        assertThat(socialAuthenticationResult.getAuthenticationToken()).isNotNull();
        assertThat(socialAuthenticationResult.getRegisterToken()).isNull();
    }

    @Test
    @DisplayName("등록되지 않은 소셜 계정이라면 RegisterToken을 발급한다.")
    void 등록되지_않은_소셜_계정이라면_RegisterToken을_발급한다() {
        //given
        String code = "socialCode";
        AccountType accountType = AccountType.KAKAO;
        String socialAccessToken = "socialAccessToken";
        String socialUid = "socialUid";

        BDDMockito.given(socialAPIManager.getAccessToken(any(), any())).willReturn(socialAccessToken);
        BDDMockito.given(socialAPIManager.getSocialUid(any(), any())).willReturn(socialUid);

        //when
        SocialAuthenticationResult socialAuthenticationResult = authenticationService.socialLogin(accountType, code);

        //then
        assertThat(socialAuthenticationResult.getIsRegistered()).isFalse();

        assertThat(socialAuthenticationResult.getRegisterToken())
            .isNotNull()
            .isEqualTo(socialAccessToken);
    }
}