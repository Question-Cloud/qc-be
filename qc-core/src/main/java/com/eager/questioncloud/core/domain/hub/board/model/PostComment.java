package com.eager.questioncloud.core.domain.hub.board.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostComment {
    private Long id;
    private Long boardId;
    private Long writerId;
    private String comment;
    private LocalDateTime createdAt;

    @Builder
    public PostComment(Long id, Long boardId, Long writerId, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.boardId = boardId;
        this.writerId = writerId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public static PostComment create(Long boardId, Long writerId, String comment) {
        return PostComment.builder()
            .boardId(boardId)
            .writerId(writerId)
            .comment(comment)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void modify(String comment) {
        this.comment = comment;
    }
}
