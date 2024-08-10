package com.eager.questioncloud.mail;

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
    private Long uid;

    @Column
    @Enumerated(EnumType.STRING)
    private EmailVerificationType emailVerificationType;

    @Column
    private Boolean isVerified;

    @Builder
    public EmailVerificationEntity(String token, Long uid, EmailVerificationType emailVerificationType, Boolean isVerified) {
        this.token = token;
        this.uid = uid;
        this.emailVerificationType = emailVerificationType;
        this.isVerified = isVerified;
    }

    public EmailVerification toDomain() {
        return EmailVerification.builder()
            .token(token)
            .uid(uid)
            .emailVerificationType(emailVerificationType)
            .isVerified(isVerified)
            .build();
    }
}
