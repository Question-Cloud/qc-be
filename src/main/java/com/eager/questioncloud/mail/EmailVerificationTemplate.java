package com.eager.questioncloud.mail;

import lombok.Getter;

@Getter
public class EmailVerificationTemplate {
    String title;
    String content;

    public EmailVerificationTemplate() {
    }
}
