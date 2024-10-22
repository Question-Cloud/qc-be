package com.eager.questioncloud.domain.verification.dto;

import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.verification.model.EmailVerification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationWithUser {
    private EmailVerification emailVerification;
    private User user;
}
