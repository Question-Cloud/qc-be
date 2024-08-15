package com.eager.questioncloud.question;

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
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionLevel questionLevel;

    @Column
    private int price;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionEntity(Long id, Long questionCategoryId, Long creatorId, Subject subject, String title, String description, String thumbnail,
        String fileUrl, QuestionType questionType, QuestionLevel questionLevel, int price, LocalDateTime createdAt) {
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
            .questionType(questionType)
            .questionLevel(questionLevel)
            .price(price)
            .createdAt(createdAt)
            .build();
    }
}
