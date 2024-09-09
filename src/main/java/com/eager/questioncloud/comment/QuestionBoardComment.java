package com.eager.questioncloud.comment;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionBoardComment {
    private Long id;
    private Long writerId;
    private String comment;
    private LocalDateTime createdAt;

    @Builder
    public QuestionBoardComment(Long id, Long writerId, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.writerId = writerId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public QuestionBoardCommentEntity toEntity() {
        return QuestionBoardCommentEntity.builder()
            .id(id)
            .writerId(writerId)
            .comment(comment)
            .createdAt(createdAt)
            .build();
    }
}
