package com.eager.questioncloud.question;

import lombok.Getter;

@Getter
public enum QuestionLevel {
    LEVEL1("LEVEL1"), LEVEL2("LEVEL2"), LEVEL3("LEVEL3"), LEVEL4("LEVEL4"), LEVEL5("LEVEL5"), LEVEL6("LEVEL6");
    private final String value;

    QuestionLevel(String value) {
        this.value = value;
    }
}
