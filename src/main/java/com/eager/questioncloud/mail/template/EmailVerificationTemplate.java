package com.eager.questioncloud.mail.template;

import lombok.Getter;

@Getter
public class EmailVerificationTemplate {
    String title;
    String content;

    public EmailVerificationTemplate() {
    }
}
