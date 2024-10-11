package com.eager.questioncloud.mail.implement;

import com.eager.questioncloud.mail.template.EmailVerificationTemplate;
import com.eager.questioncloud.mail.template.EmailVerificationTemplateCreator;
import com.eager.questioncloud.mail.dto.EmailVerificationWithUser;
import com.eager.questioncloud.mail.domain.Email;
import com.eager.questioncloud.mail.domain.EmailVerification;
import com.eager.questioncloud.mail.domain.EmailVerificationType;
import com.eager.questioncloud.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationProcessor {
    private final EmailVerificationAppender emailVerificationAppender;
    private final EmailVerificationReader emailVerificationReader;
    private final EmailVerificationUpdater emailVerificationUpdater;
    private final GoogleMailSender googleMailSender;

    public EmailVerification sendVerificationMail(User user, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationAppender.append(
            EmailVerification.create(
                user.getUid(),
                emailVerificationType)
        );
        EmailVerificationTemplate template = EmailVerificationTemplateCreator.getTemplate(emailVerificationType, emailVerification.getToken());
        googleMailSender.sendMail(
            new Email(
                user.getEmail(),
                template.getTitle(),
                template.getContent()
            ));
        return emailVerification;
    }

    public void resendVerificationMail(String resendToken) {
        EmailVerificationWithUser emailVerificationWithUser = emailVerificationReader.getForResend(resendToken);
        EmailVerification emailVerification = emailVerificationWithUser.getEmailVerification();
        User user = emailVerificationWithUser.getUser();
        EmailVerificationTemplate template = EmailVerificationTemplateCreator.getTemplate(
            emailVerification.getEmailVerificationType(),
            emailVerification.getToken());
        googleMailSender.sendMail(
            new Email(
                user.getEmail(),
                template.getTitle(),
                template.getContent())
        );
    }

    public EmailVerification verify(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationReader.get(token, emailVerificationType);
        emailVerificationUpdater.verify(emailVerification);
        return emailVerification;
    }
}
