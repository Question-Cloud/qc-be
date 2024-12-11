package com.eager.questioncloud.application.api.user.register;

import com.eager.questioncloud.application.mail.EmailSender;
import com.eager.questioncloud.core.domain.social.SocialAPIManager;
import com.eager.questioncloud.core.domain.social.SocialPlatform;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.enums.UserStatus;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.domain.user.infrastructure.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.model.UserInformation;
import com.eager.questioncloud.core.domain.verification.Email;
import com.eager.questioncloud.core.domain.verification.EmailVerification;
import com.eager.questioncloud.core.domain.verification.EmailVerificationProcessor;
import com.eager.questioncloud.core.domain.verification.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserService {
    private final UserRepository userRepository;
    private final EmailVerificationProcessor emailVerificationProcessor;
    private final SocialAPIManager socialAPIManager;
    private final EmailSender emailSender;

    //TODO UserPoint 초기화
    public User create(CreateUser createUser) {
        UserAccountInformation userAccountInformation = createUserAccountInformation(createUser);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.PendingEmailVerification);
        return userRepository.save(user);
    }

    public EmailVerification sendCreateUserVerifyMail(User user) {
        EmailVerification emailVerification = emailVerificationProcessor.createEmailVerification(
            user.getUid(),
            user.getUserInformation().getEmail(),
            EmailVerificationType.CreateUser);
        emailSender.sendMail(Email.of(emailVerification));
        return emailVerification;
    }

    public void resend(String resendToken) {
        EmailVerification emailVerification = emailVerificationProcessor.getByResendToken(resendToken);
        emailSender.sendMail(Email.of(emailVerification));
    }

    public void verifyCreateUser(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationProcessor.verifyEmailVerification(token, emailVerificationType);
        User user = userRepository.getUser(emailVerification.getUid());
        user.active();
        userRepository.save(user);
    }

    private UserAccountInformation createUserAccountInformation(CreateUser createUser) {
        if (createUser.getAccountType().equals(AccountType.EMAIL)) {
            return UserAccountInformation.createEmailAccountInformation(createUser.getPassword());
        }
        String socialUid = socialAPIManager.getSocialUid(createUser.getSocialRegisterToken(), SocialPlatform.from(createUser.getAccountType()));
        return UserAccountInformation.createSocialAccountInformation(socialUid, createUser.getAccountType());
    }
}
