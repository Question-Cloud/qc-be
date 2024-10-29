package com.eager.questioncloud.core.domain.question.vo;

import lombok.Getter;

@Getter
public enum Subject {
    Mathematics("Mathematics"), Physics("Physics"), Chemistry("Chemistry"), Biology("Biology"), EarthScience("EarthScience");
    private final String value;

    Subject(String value) {
        this.value = value;
    }
}
