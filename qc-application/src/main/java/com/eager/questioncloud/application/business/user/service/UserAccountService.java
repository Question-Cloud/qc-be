package com.eager.questioncloud.application.business.user.service;

import com.eager.questioncloud.application.mail.EmailSender;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType;
import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.core.domain.verification.model.Email;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserRepository userRepository;
    private final EmailVerificationProcessor emailVerificationProcessor;
    private final EmailSender emailSender;

    public String recoverForgottenEmail(String phone) {
        User user = userRepository.getUserByPhone(phone);
        return user.getUserInformation().getEmail();
    }

    public void sendRecoverForgottenPasswordMail(String email) {
        User user = userRepository.getUserByEmail(email);
        EmailVerification emailVerification = emailVerificationProcessor.createEmailVerification(
            user.getUid(),
            email,
            EmailVerificationType.ChangePassword);
        emailSender.sendMail(Email.of(emailVerification));
    }

    public void sendChangePasswordMail(User user) {
        EmailVerification emailVerification = emailVerificationProcessor.createEmailVerification(
            user.getUid(),
            user.getUserInformation().getEmail(),
            EmailVerificationType.ChangePassword);
        emailSender.sendMail(Email.of(emailVerification));
    }

    @Transactional
    public void changePassword(String token, String newPassword) {
        EmailVerification emailVerification = emailVerificationProcessor.verifyEmailVerification(token, EmailVerificationType.ChangePassword);
        User user = userRepository.getUser(emailVerification.getUid());
        user.changePassword(newPassword);
        userRepository.save(user);
    }
}
