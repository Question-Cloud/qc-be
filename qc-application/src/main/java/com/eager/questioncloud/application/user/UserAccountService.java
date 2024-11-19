package com.eager.questioncloud.application.user;

import com.eager.questioncloud.application.mail.EmailSender;
import com.eager.questioncloud.domain.user.User;
import com.eager.questioncloud.domain.user.UserRepository;
import com.eager.questioncloud.domain.verification.Email;
import com.eager.questioncloud.domain.verification.EmailVerification;
import com.eager.questioncloud.domain.verification.EmailVerificationRepository;
import com.eager.questioncloud.domain.verification.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailSender emailSender;

    public String recoverForgottenEmail(String phone) {
        User user = userRepository.getUserByPhone(phone);
        return user.getUserInformation().getEmail();
    }

    public void sendRecoverForgottenPasswordMail(String email) {
        User user = userRepository.getUserByEmail(email);
        EmailVerification emailVerification = EmailVerification.create(user.getUid(), EmailVerificationType.ChangePassword);
        emailVerificationRepository.save(emailVerification);

        emailSender.sendMail(Email.of(email, emailVerification));
    }

    public void sendChangePasswordMail(User user) {
        EmailVerification emailVerification = EmailVerification.create(user.getUid(), EmailVerificationType.ChangePassword);
        emailVerificationRepository.save(emailVerification);

        emailSender.sendMail(Email.of(user.getUserInformation().getEmail(), emailVerification));
    }

    @Transactional
    public void changePassword(String token, String newPassword) {
        EmailVerification emailVerification = emailVerificationRepository.get(token, EmailVerificationType.ChangePassword);
        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);

        User user = userRepository.getUser(emailVerification.getUid());
        user.changePassword(newPassword);
        userRepository.save(user);
    }
}
