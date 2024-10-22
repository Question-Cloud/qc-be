package com.eager.questioncloud.domain.user.service;

import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.user.implement.UserReader;
import com.eager.questioncloud.domain.user.implement.UserUpdater;
import com.eager.questioncloud.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.domain.verification.model.EmailVerification;
import com.eager.questioncloud.domain.verification.vo.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserReader userReader;
    private final UserUpdater userUpdater;
    private final EmailVerificationProcessor emailVerificationProcessor;

    public void sendChangePasswordMail(User user) {
        emailVerificationProcessor.sendVerificationMail(user, EmailVerificationType.ChangePassword);
    }

    public void changePassword(String token, String newPassword) {
        EmailVerification emailVerification = emailVerificationProcessor.verify(token, EmailVerificationType.ChangePassword);
        userUpdater.changePassword(userReader.getUser(emailVerification.getUid()), newPassword);
    }

    public void updateUser(User user, String name, String profileImage) {
        userUpdater.updateUser(user, name, profileImage);
    }
}
