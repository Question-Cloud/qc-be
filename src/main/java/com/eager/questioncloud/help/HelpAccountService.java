package com.eager.questioncloud.help;

import com.eager.questioncloud.mail.EmailVerification;
import com.eager.questioncloud.mail.EmailVerificationProcessor;
import com.eager.questioncloud.mail.EmailVerificationType;
import com.eager.questioncloud.user.domain.User;
import com.eager.questioncloud.user.implement.UserReader;
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
