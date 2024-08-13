package com.eager.questioncloud.mail;

import com.eager.questioncloud.user.EmailVerificationWithUser;
import com.eager.questioncloud.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserEmailVerificationProcessor {
    private final EmailVerificationRepository emailVerificationRepository;
    private final GoogleMailSender googleMailSender;

    public EmailVerification sendVerificationMail(User user) {
        EmailVerification emailVerification = emailVerificationRepository.append(
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

    public void resendVerificationMail(String resendToken) {
        EmailVerificationWithUser emailVerificationWithUser = emailVerificationRepository.findForResend(resendToken);
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
        EmailVerification emailVerification = emailVerificationRepository.find(token, emailVerificationType);
        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
        return emailVerification;
    }
}
