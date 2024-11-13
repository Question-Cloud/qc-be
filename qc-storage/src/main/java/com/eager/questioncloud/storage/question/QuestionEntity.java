package com.eager.questioncloud.storage.question;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.vo.QuestionStatus;
import com.eager.questioncloud.core.domain.question.vo.Subject;
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

    @Column
    private Long questionCategoryId;

    @Column
    @Enumerated(EnumType.STRING)
    private Subject subject;

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
    public QuestionEntity(Long id, Long creatorId, Long questionCategoryId, Subject subject, QuestionContentEntity questionContentEntity,
        QuestionStatus questionStatus, int count, LocalDateTime createdAt) {
        this.id = id;
        this.creatorId = creatorId;
        this.questionCategoryId = questionCategoryId;
        this.subject = subject;
        this.questionContentEntity = questionContentEntity;
        this.questionStatus = questionStatus;
        this.count = count;
        this.createdAt = createdAt;
    }

    public static QuestionEntity from(Question question) {
        return QuestionEntity.builder()
            .id(question.getId())
            .creatorId(question.getCreator().getId())
            .questionCategoryId(question.getCategory().getChildCategory().getId())
            .subject(question.getCategory().getChildCategory().getSubject())
            .questionContentEntity(QuestionContentEntity.from(question.getQuestionContent()))
            .questionStatus(question.getQuestionStatus())
            .count(question.getCount())
            .createdAt(question.getCreatedAt())
            .build();
    }
}
