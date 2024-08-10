package com.eager.questioncloud.user;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.mail.EmailVerificationType;
import com.eager.questioncloud.user.Request.CreateUserRequest;
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
    public DefaultResponse createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        createUserRequest.validate();
        createUserService.create(CreateUser.toDomain(createUserRequest));
        return DefaultResponse.success();
    }

    @GetMapping("/verify")
    public DefaultResponse verifyCreateUser(@RequestParam String token) {
        createUserService.verifyCreateUser(token, EmailVerificationType.CreateUser);
        return DefaultResponse.success();
    }
}
