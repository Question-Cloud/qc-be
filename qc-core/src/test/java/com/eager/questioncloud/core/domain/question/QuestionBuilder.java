package com.eager.questioncloud.core.domain.question;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class QuestionBuilder {
    private Long id;
    @Builder.Default
    private Long creatorId = 1L;
    @Builder.Default
    private QuestionContent questionContent = QuestionContent
        .builder()
        .questionCategoryId(1L)
        .subject(Subject.Biology)
        .title("questionTitle")
        .description("questionDescription")
        .thumbnail("questionThumbnail")
        .fileUrl("questionFileUrl")
        .explanationUrl("questionExplanationUrl")
        .questionType(QuestionType.Past)
        .questionLevel(QuestionLevel.LEVEL4)
        .price(1000)
        .build();
    @Builder.Default
    private QuestionStatus questionStatus = QuestionStatus.Available;
    @Builder.Default
    private int count = 100;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public Question toQuestion() {
        return Question.builder()
            .id(id)
            .creatorId(creatorId)
            .questionContent(questionContent)
            .questionStatus(questionStatus)
            .count(count)
            .createdAt(createdAt)
            .build();
    }
}
