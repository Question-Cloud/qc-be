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
    private String explanationUrl;
    private QuestionType questionType;
    private QuestionLevel questionLevel;
    private QuestionStatus questionStatus;
    private int price;
    private int count;
    private int totalRate;
    private LocalDateTime createdAt;

    @Builder
    public Question(Long id, Long questionCategoryId, Long creatorId, Subject subject, String title, String description, String thumbnail,
        String fileUrl, String explanationUrl, QuestionType questionType, QuestionLevel questionLevel, QuestionStatus questionStatus, int price,
        int count, int totalRate, LocalDateTime createdAt) {
        this.id = id;
        this.questionCategoryId = questionCategoryId;
        this.creatorId = creatorId;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.fileUrl = fileUrl;
        this.explanationUrl = explanationUrl;
        this.questionType = questionType;
        this.questionLevel = questionLevel;
        this.questionStatus = questionStatus;
        this.price = price;
        this.count = count;
        this.totalRate = totalRate;
        this.createdAt = createdAt;
    }

    public static Question create(Long creatorId, QuestionContent questionContent) {
        return Question.builder()
            .questionCategoryId(questionContent.getQuestionCategoryId())
            .creatorId(creatorId)
            .subject(questionContent.getSubject())
            .title(questionContent.getTitle())
            .description(questionContent.getDescription())
            .thumbnail(questionContent.getThumbnail())
            .fileUrl(questionContent.getFileUrl())
            .explanationUrl(questionContent.getExplanationUrl())
            .questionType(questionContent.getQuestionType())
            .questionLevel(questionContent.getQuestionLevel())
            .questionStatus(QuestionStatus.Available)
            .price(questionContent.getPrice())
            .count(0)
            .totalRate(0)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void modify(QuestionContent questionContent) {
        this.questionCategoryId = questionContent.getQuestionCategoryId();
        this.subject = questionContent.getSubject();
        this.title = questionContent.getTitle();
        this.description = questionContent.getDescription();
        this.thumbnail = questionContent.getThumbnail();
        this.fileUrl = questionContent.getFileUrl();
        this.explanationUrl = questionContent.getExplanationUrl();
        this.questionLevel = questionContent.getQuestionLevel();
        this.questionStatus = questionContent.getQuestionStatus();
        this.price = questionContent.getPrice();
    }

    public void delete() {
        this.questionStatus = QuestionStatus.Delete;
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
            .explanationUrl(explanationUrl)
            .questionType(questionType)
            .questionLevel(questionLevel)
            .questionStatus(questionStatus)
            .price(price)
            .count(count)
            .totalRate(totalRate)
            .createdAt(createdAt)
            .build();
    }
}
