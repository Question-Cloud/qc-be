package com.eager.questioncloud.core.domain.question.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionContent {
    private Long questionCategoryId;
    private Subject subject;
    private String title;
    private String description;
    private String thumbnail;
    private String fileUrl;
    private String explanationUrl;
    private QuestionType questionType;
    private QuestionLevel questionLevel;
    private int price;

    @Builder
    public QuestionContent(Long questionCategoryId, Subject subject, String title, String description, String thumbnail, String fileUrl,
        String explanationUrl, QuestionType questionType, QuestionLevel questionLevel, int price) {
        this.questionCategoryId = questionCategoryId;
        this.subject = subject;
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
