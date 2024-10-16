package com.eager.questioncloud.question.model;

import lombok.Getter;

@Getter
public enum QuestionSortType {
    Popularity("Popularity"), Rate("Rate"), Latest("Latest"), LEVEL("LEVEL");
    private final String value;

    QuestionSortType(String value) {
        this.value = value;
    }
}
