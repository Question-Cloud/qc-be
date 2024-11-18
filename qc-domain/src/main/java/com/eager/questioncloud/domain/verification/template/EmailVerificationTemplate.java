package com.eager.questioncloud.domain.verification.template;

import lombok.Getter;

@Getter
public class EmailVerificationTemplate {
    String title;
    String content;

    public EmailVerificationTemplate() {
    }
}
