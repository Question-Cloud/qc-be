package com.eager.questioncloud.application.api.user.register;

import com.eager.questioncloud.application.mail.EmailSender;
import com.eager.questioncloud.domain.social.SocialAPIManager;
import com.eager.questioncloud.domain.social.SocialPlatform;
import com.eager.questioncloud.domain.user.AccountType;
import com.eager.questioncloud.domain.user.CreateUser;
import com.eager.questioncloud.domain.user.User;
import com.eager.questioncloud.domain.user.UserAccountInformation;
import com.eager.questioncloud.domain.user.UserInformation;
import com.eager.questioncloud.domain.user.UserRepository;
import com.eager.questioncloud.domain.user.UserStatus;
import com.eager.questioncloud.domain.user.UserType;
import com.eager.questioncloud.domain.verification.Email;
import com.eager.questioncloud.domain.verification.EmailVerification;
import com.eager.questioncloud.domain.verification.EmailVerificationRepository;
import com.eager.questioncloud.domain.verification.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserService {
    //TODO 이메일 인증 관련 로직 및 Repository 별도 클래스로 분리
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final SocialAPIManager socialAPIManager;
    private final EmailSender emailSender;

    public User create(CreateUser createUser) {
        UserAccountInformation userAccountInformation = createUserAccountInformation(createUser);
        UserInformation userInformation = UserInformation.create(createUser);
        User user = User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.PendingEmailVerification);
        return userRepository.save(user);
    }

    public EmailVerification sendCreateUserVerifyMail(User user) {
        EmailVerification emailVerification = emailVerificationRepository.save(
            EmailVerification.create(user.getUid(), EmailVerificationType.CreateUser)
        );
        emailSender.sendMail(Email.of(user.getUserInformation().getEmail(), emailVerification));
        return emailVerification;
    }

    public void resend(String resendToken) {
        EmailVerification emailVerification = emailVerificationRepository.getForResend(resendToken);
        User user = userRepository.getUser(emailVerification.getUid());
        emailSender.sendMail(Email.of(user.getUserInformation().getEmail(), emailVerification));
    }

    public void verifyCreateUser(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationRepository.get(token, emailVerificationType);
        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);

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
