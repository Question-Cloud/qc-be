package com.eager.questioncloud.mail.dto;

import com.eager.questioncloud.mail.domain.EmailVerification;
import com.eager.questioncloud.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationWithUser {
    private EmailVerification emailVerification;
    private User user;
}