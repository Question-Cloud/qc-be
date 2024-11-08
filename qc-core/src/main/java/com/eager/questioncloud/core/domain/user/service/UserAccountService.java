package com.eager.questioncloud.core.domain.user.service;

import com.eager.questioncloud.core.domain.user.implement.UserReader;
import com.eager.questioncloud.core.domain.user.implement.UserUpdater;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserReader userReader;
    private final EmailVerificationProcessor emailVerificationProcessor;
    private final UserUpdater userUpdater;

    public String recoverForgottenEmail(String phone) {
        return userReader.getUserByPhone(phone).getUserInformation().getEmail();
    }

    public void sendRecoverForgottenPasswordMail(String email) {
        User user = userReader.getUserByEmail(email);
        emailVerificationProcessor.sendVerificationMail(user, EmailVerificationType.ChangePassword);
    }

    public void sendChangePasswordMail(User user) {
        emailVerificationProcessor.sendVerificationMail(user, EmailVerificationType.ChangePassword);
    }

    public void changePassword(String token, String newPassword) {
        EmailVerification emailVerification = emailVerificationProcessor.verify(token, EmailVerificationType.ChangePassword);
        userUpdater.changePassword(userReader.getUser(emailVerification.getUid()), newPassword);
    }
}
