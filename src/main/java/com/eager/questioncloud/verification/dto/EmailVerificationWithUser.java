package com.eager.questioncloud.verification.dto;

import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.verification.model.EmailVerification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationWithUser {
    private EmailVerification emailVerification;
    private User user;
}
