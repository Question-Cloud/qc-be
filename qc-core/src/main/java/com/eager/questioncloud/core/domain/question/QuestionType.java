package com.eager.questioncloud.core.domain.question;

import lombok.Getter;

@Getter
public enum QuestionType {
    Past("Past"), SelfMade("SelfMade");
    private final String value;

    QuestionType(String value) {
        this.value = value;
    }
}
