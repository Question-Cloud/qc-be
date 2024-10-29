package com.eager.questioncloud.storage.verification;

import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "email_verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationEntity {
    @Id
    private String token;

    @Column
    private String resendToken;

    @Column
    private Long uid;

    @Column
    @Enumerated(EnumType.STRING)
    private EmailVerificationType emailVerificationType;

    @Column
    private Boolean isVerified;

    @Builder
    public EmailVerificationEntity(String token, String resendToken, Long uid, EmailVerificationType emailVerificationType, Boolean isVerified) {
        this.token = token;
        this.resendToken = resendToken;
        this.uid = uid;
        this.emailVerificationType = emailVerificationType;
        this.isVerified = isVerified;
    }

    public EmailVerification toModel() {
        return EmailVerification.builder()
            .token(token)
            .resendToken(resendToken)
            .uid(uid)
            .emailVerificationType(emailVerificationType)
            .isVerified(isVerified)
            .build();
    }

    public static EmailVerificationEntity from(EmailVerification emailVerification) {
        return EmailVerificationEntity.builder()
            .token(emailVerification.getToken())
            .resendToken(emailVerification.getResendToken())
            .uid(emailVerification.getUid())
            .emailVerificationType(emailVerification.getEmailVerificationType())
            .isVerified(emailVerification.getIsVerified())
            .build();
    }
}
