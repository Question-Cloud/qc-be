package com.eager.questioncloud.core.domain.hub.question.vo;

import lombok.Getter;

@Getter
public enum QuestionStatus {
    Available("Available"), UnAvailable("UnAvailable"), Delete("Delete");
    private final String value;

    QuestionStatus(String value) {
        this.value = value;
    }
}
