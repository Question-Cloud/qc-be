package com.eager.questioncloud.storage.question;

import com.eager.questioncloud.core.domain.hub.post.model.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long questionId;

    @Column
    private Long writerId;

    @Embedded
    private PostContentEntity postContentEntity;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public PostEntity(Long id, Long questionId, Long writerId, PostContentEntity postContentEntity, LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.postContentEntity = postContentEntity;
        this.createdAt = createdAt;
    }

    public Post toModel() {
        return Post.builder()
            .id(id)
            .questionId(questionId)
            .writerId(writerId)
            .postContent(postContentEntity.toModel())
            .createdAt(createdAt)
            .build();
    }

    public static PostEntity from(Post post) {
        return PostEntity.builder()
            .id(post.getId())
            .questionId(post.getQuestionId())
            .writerId(post.getWriterId())
            .postContentEntity(PostContentEntity.from(post.getPostContent()))
            .createdAt(post.getCreatedAt())
            .build();
    }
}
