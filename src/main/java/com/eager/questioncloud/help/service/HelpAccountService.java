package com.eager.questioncloud.help.service;

import com.eager.questioncloud.user.implement.UserReader;
import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.verification.model.EmailVerification;
import com.eager.questioncloud.verification.vo.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpAccountService {
    private final UserReader userReader;
    private final EmailVerificationProcessor emailVerificationProcessor;

    public String recoverForgottenEmail(String phone) {
        return userReader.getUserByPhone(phone).getUserInformation().getEmail();
    }

    public EmailVerification sendRecoverForgottenPasswordMail(String email) {
        User user = userReader.getUserByEmail(email);
        return emailVerificationProcessor.sendVerificationMail(user, EmailVerificationType.ChangePassword);
    }
}
