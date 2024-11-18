package com.eager.questioncloud.domain.review.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionReview {
    private Long id;
    private Long questionId;
    private Long reviewerId;
    private String comment;
    private int rate;
    private LocalDateTime createdAt;
    private Boolean isDeleted;

    @Builder
    public QuestionReview(Long id, Long questionId, Long reviewerId, String comment, int rate, LocalDateTime createdAt, Boolean isDeleted) {
        this.id = id;
        this.questionId = questionId;
        this.reviewerId = reviewerId;
        this.comment = comment;
        this.rate = rate;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public void modify(String comment, int rate) {
        this.comment = comment;
        this.rate = rate;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public static QuestionReview create(Long questionId, Long reviewerId, String comment, int rate) {
        return QuestionReview.builder()
            .questionId(questionId)
            .reviewerId(reviewerId)
            .comment(comment)
            .rate(rate)
            .createdAt(LocalDateTime.now())
            .isDeleted(false)
            .build();
    }
}
