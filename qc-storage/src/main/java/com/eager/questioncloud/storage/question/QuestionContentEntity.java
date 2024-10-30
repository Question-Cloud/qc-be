package com.eager.questioncloud.storage.question;

import com.eager.questioncloud.core.domain.hub.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionLevel;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionType;
import com.eager.questioncloud.core.domain.hub.question.vo.Subject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionContentEntity {
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

    public static QuestionContentEntity from(QuestionContent questionContent) {
        return QuestionContentEntity
            .builder()
            .questionCategoryId(questionContent.getQuestionCategoryId())
            .subject(questionContent.getSubject())
            .title(questionContent.getTitle())
            .description(questionContent.getDescription())
            .thumbnail(questionContent.getThumbnail())
            .fileUrl(questionContent.getFileUrl())
            .explanationUrl(questionContent.getExplanationUrl())
            .questionType(questionContent.getQuestionType())
            .questionLevel(questionContent.getQuestionLevel())
            .price(questionContent.getPrice())
            .build();
    }

    public QuestionContent toModel() {
        return QuestionContent.builder()
            .questionCategoryId(questionCategoryId)
            .subject(subject)
            .title(title)
            .description(description)
            .thumbnail(thumbnail)
            .fileUrl(fileUrl)
            .explanationUrl(explanationUrl)
            .questionType(questionType)
            .questionLevel(questionLevel)
            .price(price)
            .build();
    }
}
