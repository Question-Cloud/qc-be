package com.eager.questioncloud.verification.dto;

import com.eager.questioncloud.verification.domain.EmailVerification;
import com.eager.questioncloud.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationWithUser {
    private EmailVerification emailVerification;
    private User user;
}
