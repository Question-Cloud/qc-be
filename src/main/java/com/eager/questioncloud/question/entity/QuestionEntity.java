package com.eager.questioncloud.question.entity;

import com.eager.questioncloud.question.model.Question;
import com.eager.questioncloud.question.vo.QuestionContent;
import com.eager.questioncloud.question.vo.QuestionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
    private Long creatorId;

    @Embedded
    private QuestionContent questionContent;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @Column
    private int count;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionEntity(Long id, Long creatorId, QuestionContent questionContent, QuestionStatus questionStatus, int count,
        LocalDateTime createdAt) {
        this.id = id;
        this.creatorId = creatorId;
        this.questionContent = questionContent;
        this.questionStatus = questionStatus;
        this.count = count;
        this.createdAt = createdAt;
    }

    public Question toDomain() {
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
