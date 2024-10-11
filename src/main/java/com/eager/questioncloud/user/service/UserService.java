package com.eager.questioncloud.user.service;

import com.eager.questioncloud.mail.domain.EmailVerification;
import com.eager.questioncloud.mail.implement.EmailVerificationProcessor;
import com.eager.questioncloud.mail.domain.EmailVerificationType;
import com.eager.questioncloud.user.domain.AccountType;
import com.eager.questioncloud.user.domain.User;
import com.eager.questioncloud.user.implement.UserReader;
import com.eager.questioncloud.user.implement.UserUpdater;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserReader userReader;
    private final UserUpdater userUpdater;
    private final EmailVerificationProcessor emailVerificationProcessor;

    public Optional<User> getSocialUser(AccountType accountType, String socialUid) {
        return userReader.getSocialUser(accountType, socialUid);
    }

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
