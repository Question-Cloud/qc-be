package com.eager.questioncloud.core.domain.question.enums;

import lombok.Getter;

@Getter
public enum QuestionStatus {
    Available("Available"), UnAvailable("UnAvailable"), Delete("Delete");
    private final String value;

    QuestionStatus(String value) {
        this.value = value;
    }
}
