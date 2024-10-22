package com.eager.questioncloud.domain.help.service;

import com.eager.questioncloud.domain.user.implement.UserReader;
import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.domain.verification.model.EmailVerification;
import com.eager.questioncloud.domain.verification.vo.EmailVerificationType;
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
