package com.eager.questioncloud.mail.domain;

import com.eager.questioncloud.mail.entity.EmailVerificationEntity;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVerification {
    private String token;
    private String resendToken;
    private Long uid;
    private EmailVerificationType emailVerificationType;
    private Boolean isVerified;

    @Builder
    public EmailVerification(String token, String resendToken, Long uid, EmailVerificationType emailVerificationType, Boolean isVerified) {
        this.token = token;
        this.resendToken = resendToken;
        this.uid = uid;
        this.emailVerificationType = emailVerificationType;
        this.isVerified = isVerified;
    }

    public void verify() {
        this.isVerified = true;
    }

    public static EmailVerification create(Long uid, EmailVerificationType emailVerificationType) {
        return EmailVerification.builder()
            .token(UUID.randomUUID().toString())
            .resendToken(UUID.randomUUID().toString())
            .uid(uid)
            .emailVerificationType(emailVerificationType)
            .isVerified(false)
            .build();
    }

    public EmailVerificationEntity toEntity() {
        return EmailVerificationEntity.builder()
            .token(token)
            .resendToken(resendToken)
            .uid(uid)
            .emailVerificationType(emailVerificationType)
            .isVerified(isVerified)
            .build();
    }
}