package com.eager.questioncloud.domain.question.vo;

import com.eager.questioncloud.domain.question.dto.Request.SelfMadeQuestionRequest;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QuestionContent {
    private Long questionCategoryId;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    private String title;

    private String description;

    private String thumbnail;

    private String fileUrl;

    private String explanationUrl;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private QuestionLevel questionLevel;

    private int price;

    public static QuestionContent createSelfMadeQuestionContent(SelfMadeQuestionRequest request) {
        return new QuestionContent(
            request.getQuestionCategoryId(),
            request.getSubject(),
            request.getTitle(),
            request.getDescription(),
            request.getThumbnail(),
            request.getFileUrl(),
            request.getExplanationUrl(),
            QuestionType.SelfMade,
            request.getQuestionLevel(),
            request.getPrice()
        );
    }
}
