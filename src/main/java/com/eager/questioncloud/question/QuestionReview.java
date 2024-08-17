package com.eager.questioncloud.question;

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
}
