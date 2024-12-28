package com.eager.questioncloud.application.api.user.register.service;

import com.eager.questioncloud.application.mail.EmailSender;
import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.enums.UserStatus;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.model.UserInformation;
import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType;
import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.core.domain.verification.model.Email;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.social.SocialAPIManager;
import com.eager.questioncloud.social.SocialPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RegisterUserService {
    private final UserRepository userRepository;
    private final EmailVerificationProcessor emailVerificationProcessor;
    private final UserPointManager userPointManager;
    private final SocialAPIManager socialAPIManager;
    private final EmailSender emailSender;

    @Transactional
    public User create(CreateUser createUser) {
        UserAccountInformation userAccountInformation = createUserAccountInformation(createUser);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = userRepository.save(
            User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.PendingEmailVerification)
        );
        userPointManager.init(user.getUid());
        return user;
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
        String socialUid = socialAPIManager.getSocialUid(
            createUser.getSocialRegisterToken(),
            SocialPlatform.valueOf(createUser.getAccountType().getValue())
        );
        return UserAccountInformation.createSocialAccountInformation(socialUid, createUser.getAccountType());
    }
}
