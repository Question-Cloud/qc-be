package com.eager.questioncloud.api.user.register;

import com.eager.questioncloud.api.user.register.Response.CreateUserResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.service.CreateUserService;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/register")
@RequiredArgsConstructor
public class CreateUserController {
    private final CreateUserService createUserService;

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "회원가입", summary = "회원가입", tags = {"user-register"}, description = "회원가입")
    public CreateUserResponse createUser(@RequestBody @Valid Request.CreateUserRequest request) {
        User user = createUserService.create(
            new CreateUser(request.getEmail(), request.getPassword(), request.getSocialRegisterToken(), request.getAccountType(), request.getPhone(),
                request.getName())
        );
        EmailVerification emailVerification = createUserService.sendCreateUserVerifyMail(user);
        return new CreateUserResponse(emailVerification.getResendToken());
    }

    @GetMapping("/resend-verification-mail")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "회원가입 인증 메일 재요청", summary = "회원가입 인증 메일 재요청", tags = {"user-register"}, description = "회원가입 인증 메일 재요청")
    public DefaultResponse resendVerificationMail(@RequestParam String resendToken) {
        createUserService.resend(resendToken);
        return DefaultResponse.success();
    }

    @GetMapping("/verify")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "회원가입 인증 메일 확인", summary = "회원가입 인증 메일 확인", tags = {"user-register"}, description = "회원가입 인증 메일 확인")
    public DefaultResponse verifyCreateUser(@RequestParam String token) {
        createUserService.verifyCreateUser(token, EmailVerificationType.CreateUser);
        return DefaultResponse.success();
    }
}
