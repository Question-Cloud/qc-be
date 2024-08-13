package com.eager.questioncloud.user;

import com.eager.questioncloud.mail.EmailVerification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationWithUser {
    private EmailVerification emailVerification;
    private User user;
}
