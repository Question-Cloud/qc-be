package com.eager.questioncloud.help;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.help.Response.RecoverForgottenEmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/help")
@RequiredArgsConstructor
public class HelpAccountController {
    private final HelpAccountService helpAccountService;

    @GetMapping("/recover/email")
    public RecoverForgottenEmailResponse recoverForgottenEmail(@RequestParam String phone) {
        String email = helpAccountService.recoverForgottenEmail(phone);
        return new RecoverForgottenEmailResponse(email);
    }

    @GetMapping("/recover/password")
    public DefaultResponse sendRecoverForgottenPasswordMail(@RequestParam String email) {
        helpAccountService.sendRecoverForgottenPasswordMail(email);
        return DefaultResponse.success();
    }
}
