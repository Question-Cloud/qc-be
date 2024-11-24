package com.eager.questioncloud.core.domain.question;

import lombok.Getter;

@Getter
public enum QuestionSortType {
    Popularity("Popularity"), Rate("Rate"), Latest("Latest"), LEVEL("LEVEL");
    private final String value;

    QuestionSortType(String value) {
        this.value = value;
    }
}
