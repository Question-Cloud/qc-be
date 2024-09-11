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

    public static QuestionContent of(Question question) {
        return new QuestionContent(
            question.getQuestionCategoryId(),
            question.getSubject(),
            question.getTitle(),
            question.getDescription(),
            question.getThumbnail(),
            question.getFileUrl(),
            question.getExplanationUrl(),
            question.getQuestionType(),
            question.getQuestionLevel(),
            question.getQuestionStatus(),
            question.getPrice());
    }
}
