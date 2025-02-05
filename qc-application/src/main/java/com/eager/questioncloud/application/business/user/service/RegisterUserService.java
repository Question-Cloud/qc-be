package com.eager.questioncloud.application.business.user.service;

import com.eager.questioncloud.application.business.user.implement.UserRegister;
import com.eager.questioncloud.application.mail.EmailSender;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType;
import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.core.domain.verification.model.Email;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserService {
    private final EmailVerificationProcessor emailVerificationProcessor;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final UserRegister userRegister;

    public User create(CreateUser createUser) {
        return userRegister.create(createUser);
    }

    public EmailVerification sendCreateUserVerifyMail(User user) {
        EmailVerification emailVerification = emailVerificationProcessor.createEmailVerification(
            user.getUid(),
            user.getUserInformation().getEmail(),
            EmailVerificationType.CreateUser);
        emailSender.sendMail(Email.of(emailVerification));
        return emailVerification;
    }

    public void resend(String resendToken) {
        EmailVerification emailVerification = emailVerificationProcessor.getByResendToken(resendToken);
        emailSender.sendMail(Email.of(emailVerification));
    }

    public void verifyCreateUser(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationProcessor.verifyEmailVerification(token, emailVerificationType);
        User user = userRepository.getUser(emailVerification.getUid());
        user.active();
        userRepository.save(user);
    }
}
