package com.eager.questioncloud.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
    private QuestionStatus questionStatus;
    private int price;
}
