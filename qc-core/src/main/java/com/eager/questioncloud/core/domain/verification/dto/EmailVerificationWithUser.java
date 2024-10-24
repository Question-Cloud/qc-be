package com.eager.questioncloud.core.domain.verification.dto;

import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationWithUser {
    private EmailVerification emailVerification;
    private User user;
}
