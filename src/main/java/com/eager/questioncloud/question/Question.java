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
    private String thumbnail;
    private String fileUrl;
    private QuestionType questionType;
    private QuestionLevel questionLevel;
    private int price;
    private int count;
    private LocalDateTime createdAt;

    @Builder
    public Question(Long id, Long questionCategoryId, Long creatorId, Subject subject, String title, String description, String thumbnail,
        String fileUrl, QuestionType questionType, QuestionLevel questionLevel, int price, int count, LocalDateTime createdAt) {
        this.id = id;
        this.questionCategoryId = questionCategoryId;
        this.creatorId = creatorId;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.fileUrl = fileUrl;
        this.questionType = questionType;
        this.questionLevel = questionLevel;
        this.price = price;
        this.count = count;
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
            .thumbnail(thumbnail)
            .fileUrl(fileUrl)
            .questionType(questionType)
            .questionLevel(questionLevel)
            .price(price)
            .count(count)
            .createdAt(createdAt)
            .build();
    }
}
