package com.eager.questioncloud.domain.post;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostComment {
    private Long id;
    private Long postId;
    private Long writerId;
    private String comment;
    private LocalDateTime createdAt;

    @Builder
    public PostComment(Long id, Long postId, Long writerId, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.writerId = writerId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public static PostComment create(Long postId, Long writerId, String comment) {
        return PostComment.builder()
            .postId(postId)
            .writerId(writerId)
            .comment(comment)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void modify(String comment) {
        this.comment = comment;
    }
}
