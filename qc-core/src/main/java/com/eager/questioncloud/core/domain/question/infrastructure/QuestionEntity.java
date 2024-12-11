package com.eager.questioncloud.core.domain.question.infrastructure;

import com.eager.questioncloud.core.domain.question.enums.QuestionStatus;
import com.eager.questioncloud.core.domain.question.model.Question;
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
    private QuestionContentEntity questionContentEntity;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @Column
    private int count;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionEntity(Long id, Long creatorId, QuestionContentEntity questionContentEntity, QuestionStatus questionStatus, int count,
        LocalDateTime createdAt) {
        this.id = id;
        this.creatorId = creatorId;
        this.questionContentEntity = questionContentEntity;
        this.questionStatus = questionStatus;
        this.count = count;
        this.createdAt = createdAt;
    }

    public Question toModel() {
        return Question.builder()
            .id(id)
            .creatorId(creatorId)
            .questionContent(questionContentEntity.toModel())
            .questionStatus(questionStatus)
            .count(count)
            .createdAt(createdAt)
            .build();
    }

    public static QuestionEntity from(Question question) {
        return QuestionEntity.builder()
            .id(question.getId())
            .creatorId(question.getCreatorId())
            .questionContentEntity(QuestionContentEntity.from(question.getQuestionContent()))
            .questionStatus(question.getQuestionStatus())
            .count(question.getCount())
            .createdAt(question.getCreatedAt())
            .build();
    }
}
