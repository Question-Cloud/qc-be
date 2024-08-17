package com.eager.questioncloud.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "question_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long questionId;

    @Column
    private Long reviewerId;

    @Column
    private String comment;

    @Column
    private int rate;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Boolean isDeleted;

    @Builder
    public QuestionReviewEntity(Long id, Long questionId, Long reviewerId, String comment, int rate, LocalDateTime createdAt, Boolean isDeleted) {
        this.id = id;
        this.questionId = questionId;
        this.reviewerId = reviewerId;
        this.comment = comment;
        this.rate = rate;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }
}
