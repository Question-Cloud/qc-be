package com.eager.questioncloud.help;

import com.eager.questioncloud.help.Request.RecoverForgottenEmailRequest;
import com.eager.questioncloud.help.Response.RecoverForgottenEmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/help")
@RequiredArgsConstructor
public class HelpAccountController {
    private final HelpAccountService helpAccountService;

    @GetMapping("/recover/forgotten-email")
    public RecoverForgottenEmailResponse recoverForgottenEmail(@RequestBody RecoverForgottenEmailRequest request) {
        String email = helpAccountService.recoverForgottenEmail(request.getPhone());
        return new RecoverForgottenEmailResponse(email);
    }
}
