package com.eager.questioncloud.core.domain.post;

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
@Table(name = "post_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long postId;

    @Column
    private Long writerId;

    @Column
    private String comment;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public PostCommentEntity(Long id, Long postId, Long writerId, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.writerId = writerId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public static PostCommentEntity from(PostComment postComment) {
        return PostCommentEntity.builder()
            .id(postComment.getId())
            .postId(postComment.getPostId())
            .writerId(postComment.getWriterId())
            .comment(postComment.getComment())
            .createdAt(postComment.getCreatedAt())
            .build();
    }

    public PostComment toModel() {
        return PostComment.builder()
            .id(id)
            .postId(postId)
            .writerId(writerId)
            .comment(comment)
            .createdAt(createdAt)
            .build();
    }
}
