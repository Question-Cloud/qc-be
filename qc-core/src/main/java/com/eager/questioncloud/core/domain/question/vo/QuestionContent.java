package com.eager.questioncloud.core.domain.question.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionContent {
    private String title;
    private String description;
    private String thumbnail;
    private String fileUrl;
    private String explanationUrl;
    private QuestionType questionType;
    private QuestionLevel questionLevel;
    private int price;

    @Builder
    public QuestionContent(String title, String description, String thumbnail, String fileUrl, String explanationUrl, QuestionType questionType,
        QuestionLevel questionLevel, int price) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.fileUrl = fileUrl;
        this.explanationUrl = explanationUrl;
        this.questionType = questionType;
        this.questionLevel = questionLevel;
        this.price = price;
    }
}
