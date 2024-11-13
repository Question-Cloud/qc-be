package com.eager.questioncloud.core.domain.question.model;

import lombok.Getter;

@Getter
public class QuestionCategoryInformation {
    private QuestionCategory parentCategory;
    private QuestionCategory childCategory;

    public QuestionCategoryInformation(QuestionCategory parentCategory, QuestionCategory childCategory) {
        this.parentCategory = parentCategory;
        this.childCategory = childCategory;
    }
}
