package com.eager.questioncloud.user;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.mail.EmailVerification;
import com.eager.questioncloud.mail.EmailVerificationType;
import com.eager.questioncloud.user.Request.CreateUserRequest;
import com.eager.questioncloud.user.Response.CreateUserResponse;
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
    public CreateUserResponse createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        createUserRequest.validate();
        User user = createUserService.create(CreateUser.toDomain(createUserRequest));
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
