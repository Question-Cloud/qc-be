package com.eager.questioncloud.mail;

import com.eager.questioncloud.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserEmailVerificationProcessor {
    private final EmailVerificationAppender emailVerificationAppender;
    private final EmailVerificationReader emailVerificationReader;
    private final EmailVerificationUpdater emailVerificationUpdater;
    private final GoogleMailSender googleMailSender;

    public EmailVerification sendVerificationMail(User user) {
        EmailVerification emailVerification = emailVerificationAppender.append(
            EmailVerification.create(
                user.getUid(),
                EmailVerificationType.CreateUser)
        );
        googleMailSender.sendMail(
            new Email(
                user.getEmail(),
                CreateUserEmailVerificationTemplate.title,
                CreateUserEmailVerificationTemplate.generate(emailVerification.getToken()))
        );
        return emailVerification;
    }

    public EmailVerification getForException(Long uid) {
        return emailVerificationReader.getForNotVerifiedUser(uid);
    }

    public void resendVerificationMail(String resendToken) {
        EmailVerificationWithUser emailVerificationWithUser = emailVerificationReader.getForResend(resendToken);
        EmailVerification emailVerification = emailVerificationWithUser.getEmailVerification();
        User user = emailVerificationWithUser.getUser();
        googleMailSender.sendMail(
            new Email(
                user.getEmail(),
                CreateUserEmailVerificationTemplate.title,
                CreateUserEmailVerificationTemplate.generate(emailVerification.getToken())
            )
        );
    }

    public EmailVerification verify(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationReader.get(token, emailVerificationType);
        emailVerificationUpdater.verify(emailVerification);
        return emailVerification;
    }
}
