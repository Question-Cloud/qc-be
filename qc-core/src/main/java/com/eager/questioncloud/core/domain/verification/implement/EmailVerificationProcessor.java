package com.eager.questioncloud.core.domain.verification.implement;

import com.eager.questioncloud.core.domain.user.implement.UserReader;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.repository.EmailVerificationRepository;
import com.eager.questioncloud.core.domain.verification.template.EmailVerificationTemplate;
import com.eager.questioncloud.core.domain.verification.template.EmailVerificationTemplateCreator;
import com.eager.questioncloud.core.domain.verification.vo.Email;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationProcessor {
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserReader userReader;
    private final GoogleMailSender googleMailSender;

    public EmailVerification sendVerificationMail(User user, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationRepository.save(
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
        EmailVerification emailVerification = emailVerificationRepository.findByResendToken(resendToken);
        User user = userReader.getUser(emailVerification.getUid());
        EmailVerificationTemplate template = EmailVerificationTemplateCreator.getTemplate(
            emailVerification.getEmailVerificationType(),
            emailVerification.getToken());

        emailVerification.checkAvailableResend();
        googleMailSender.sendMail(
            new Email(
                user.getUserInformation().getEmail(),
                template.getTitle(),
                template.getContent())
        );
    }

    public EmailVerification verify(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationRepository.get(token, emailVerificationType);
        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
        return emailVerification;
    }
}
