package com.eager.questioncloud.api.user;

import com.eager.questioncloud.api.user.Response.CreateUserResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.service.CreateUserService;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class CreateUserController {
    private final CreateUserService createUserService;

    @PostMapping
    public CreateUserResponse createUser(@RequestBody @Valid Request.CreateUserRequest request) {
        User user = createUserService.create(
            new CreateUser(request.getEmail(), request.getPassword(), request.getSocialRegisterToken(), request.getAccountType(), request.getPhone(),
                request.getName())
        );
        EmailVerification emailVerification = createUserService.sendCreateUserVerifyMail(user);
        return new CreateUserResponse(emailVerification.getResendToken());
    }

    @GetMapping("/resend-verification-mail")
    public DefaultResponse resendVerificationMail(@RequestParam String resendToken) {
        createUserService.resend(resendToken);
        return DefaultResponse.success();
    }

    @GetMapping("/verify")
    public DefaultResponse verifyCreateUser(@RequestParam String token) {
        createUserService.verifyCreateUser(token, EmailVerificationType.CreateUser);
        return DefaultResponse.success();
    }
}
