package com.eager.questioncloud.question;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Question {
    private Long id;
    private Long questionCategoryId;
    private Long creatorId;
    private Subject subject;
    private String title;
    private String description;
    private String fileUrl;
    private QuestionLevel questionLevel;
    private int price;
    private LocalDateTime createdAt;

    @Builder
    public Question(Long id, Long questionCategoryId, Long creatorId, Subject subject, String title, String description, String fileUrl,
        QuestionLevel questionLevel, int price, LocalDateTime createdAt) {
        this.id = id;
        this.questionCategoryId = questionCategoryId;
        this.creatorId = creatorId;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
        this.questionLevel = questionLevel;
        this.price = price;
        this.createdAt = createdAt;
    }

    public QuestionEntity toEntity() {
        return QuestionEntity.builder()
            .id(id)
            .questionCategoryId(questionCategoryId)
            .creatorId(creatorId)
            .subject(subject)
            .title(title)
            .description(description)
            .fileUrl(fileUrl)
            .questionLevel(questionLevel)
            .price(price)
            .createdAt(createdAt)
            .build();
    }
}
