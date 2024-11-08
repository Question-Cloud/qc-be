package com.eager.questioncloud.core.domain.user.service;

import com.eager.questioncloud.core.domain.social.SocialAPIManager;
import com.eager.questioncloud.core.domain.social.SocialPlatform;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.implement.UserCreator;
import com.eager.questioncloud.core.domain.user.implement.UserReader;
import com.eager.questioncloud.core.domain.user.implement.UserUpdater;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import com.eager.questioncloud.core.domain.user.vo.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.vo.UserInformation;
import com.eager.questioncloud.core.domain.user.vo.UserStatus;
import com.eager.questioncloud.core.domain.user.vo.UserType;
import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserService {
    private final UserCreator userCreator;
    private final UserUpdater userUpdater;
    private final UserReader userReader;
    private final SocialAPIManager socialAPIManager;
    private final EmailVerificationProcessor emailVerificationProcessor;

    public User create(CreateUser createUser) {
        UserAccountInformation userAccountInformation = createUserAccountInformation(createUser);
        UserInformation userInformation = UserInformation.create(createUser);
        return userCreator.register(User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.PendingEmailVerification));
    }

    public EmailVerification sendCreateUserVerifyMail(User user) {
        return emailVerificationProcessor.sendVerificationMail(user, EmailVerificationType.CreateUser);
    }

    public void resend(String resendToken) {
        emailVerificationProcessor.resendVerificationMail(resendToken);
    }

    public void verifyCreateUser(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationProcessor.verify(token, emailVerificationType);
        User user = userReader.getUser(emailVerification.getUid());
        userUpdater.verifyUser(user);
    }

    private UserAccountInformation createUserAccountInformation(CreateUser createUser) {
        if (createUser.accountType().equals(AccountType.EMAIL)) {
            return UserAccountInformation.createEmailAccountInformation(createUser.password());
        }
        String socialUid = socialAPIManager.getSocialUid(createUser.socialRegisterToken(), SocialPlatform.from(createUser.accountType()));
        return UserAccountInformation.createSocialAccountInformation(socialUid, createUser.accountType());
    }
}
