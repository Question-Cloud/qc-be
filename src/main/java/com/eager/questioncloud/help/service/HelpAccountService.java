package com.eager.questioncloud.help.service;

import com.eager.questioncloud.mail.domain.EmailVerification;
import com.eager.questioncloud.mail.domain.EmailVerificationType;
import com.eager.questioncloud.mail.implement.EmailVerificationProcessor;
import com.eager.questioncloud.user.implement.UserReader;
import com.eager.questioncloud.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpAccountService {
    private final UserReader userReader;
    private final EmailVerificationProcessor emailVerificationProcessor;

    public String recoverForgottenEmail(String phone) {
        return userReader.getUserByPhone(phone).getEmail();
    }

    public EmailVerification sendRecoverForgottenPasswordMail(String email) {
        User user = userReader.getUserByEmail(email);
        return emailVerificationProcessor.sendVerificationMail(user, EmailVerificationType.ChangePassword);
    }
}
