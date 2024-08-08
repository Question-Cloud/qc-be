package com.eager.questioncloud.user;

import com.eager.questioncloud.mail.CreateUserEmailVerificationTemplate;
import com.eager.questioncloud.mail.Email;
import com.eager.questioncloud.mail.GoogleMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationCreator emailVerificationCreator;
    private final GoogleMailSender googleMailSender;

    public void sendVerificationEmail(User user) {
        EmailVerification emailVerification = emailVerificationCreator.append(
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
    }
}
