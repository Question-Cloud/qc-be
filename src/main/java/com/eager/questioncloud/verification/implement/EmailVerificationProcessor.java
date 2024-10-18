package com.eager.questioncloud.verification.implement;

import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.verification.dto.EmailVerificationWithUser;
import com.eager.questioncloud.verification.model.Email;
import com.eager.questioncloud.verification.model.EmailVerification;
import com.eager.questioncloud.verification.model.EmailVerificationType;
import com.eager.questioncloud.verification.template.EmailVerificationTemplate;
import com.eager.questioncloud.verification.template.EmailVerificationTemplateCreator;
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
                user.getUserInformation().getEmail(),
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
                user.getUserInformation().getEmail(),
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
