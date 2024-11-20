package com.eager.questioncloud.domain.verification;

import lombok.Getter;

@Getter
public class Email {
    private String to;
    private String subject;
    private String content;

    public Email(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public static Email of(EmailVerification emailVerification) {
        EmailVerificationTemplate template = EmailVerificationTemplateCreator.getTemplate(
            emailVerification.getEmailVerificationType(),
            emailVerification.getToken());

        return new Email(emailVerification.getEmail(), template.getTitle(), template.getContent());
    }
}
