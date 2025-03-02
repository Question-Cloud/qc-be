package com.eager.questioncloud.core.domain.verification.model;

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVerification {
    private String token;
    private String resendToken;
    private Long uid;
    private String email;
    private EmailVerificationType emailVerificationType;
    private Boolean isVerified;

    @Builder
    public EmailVerification(String token, String resendToken, Long uid, String email, EmailVerificationType emailVerificationType,
        Boolean isVerified) {
        this.token = token;
        this.resendToken = resendToken;
        this.uid = uid;
        this.email = email;
        this.emailVerificationType = emailVerificationType;
        this.isVerified = isVerified;
    }

    public void verify() {
        this.isVerified = true;
    }

    public static EmailVerification create(Long uid, String email, EmailVerificationType emailVerificationType) {
        return EmailVerification.builder()
            .token(UUID.randomUUID().toString())
            .resendToken(UUID.randomUUID().toString())
            .uid(uid)
            .email(email)
            .emailVerificationType(emailVerificationType)
            .isVerified(false)
            .build();
    }
}
