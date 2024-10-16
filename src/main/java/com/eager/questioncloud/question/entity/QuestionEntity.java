package com.eager.questioncloud.question.entity;

import com.eager.questioncloud.question.model.Question;
import com.eager.questioncloud.question.model.QuestionLevel;
import com.eager.questioncloud.question.model.QuestionStatus;
import com.eager.questioncloud.question.model.QuestionType;
import com.eager.questioncloud.question.model.Subject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long questionCategoryId;

    @Column
    private Long creatorId;

    @Column
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String thumbnail;

    @Column
    private String fileUrl;

    @Column
    private String explanationUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionLevel questionLevel;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @Column
    private int price;

    @Column
    private int count;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionEntity(Long id, Long questionCategoryId, Long creatorId, Subject subject, String title, String description, String thumbnail,
        String fileUrl, String explanationUrl, QuestionType questionType, QuestionLevel questionLevel, QuestionStatus questionStatus, int price,
        int count, LocalDateTime createdAt) {
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
        this.createdAt = createdAt;
    }

    public Question toDomain() {
        return Question.builder()
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
            .createdAt(createdAt)
            .build();
    }
}
