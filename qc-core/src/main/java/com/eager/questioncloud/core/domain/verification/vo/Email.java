package com.eager.questioncloud.core.domain.verification.vo;

import lombok.Getter;

@Getter
public class Email {
    private String to;
    private String subject;
    private String content;

    public Email(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }
}
