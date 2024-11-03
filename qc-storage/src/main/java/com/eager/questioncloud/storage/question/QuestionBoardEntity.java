package com.eager.questioncloud.storage.question;

import com.eager.questioncloud.core.domain.hub.board.model.Post;
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
@Table(name = "question_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long questionId;

    @Column
    private Long writerId;

    @Embedded
    private QuestionBoardContentEntity questionBoardContentEntity;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionBoardEntity(Long id, Long questionId, Long writerId, QuestionBoardContentEntity questionBoardContentEntity,
        LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.questionBoardContentEntity = questionBoardContentEntity;
        this.createdAt = createdAt;
    }

    public Post toModel() {
        return Post.builder()
            .id(id)
            .questionId(questionId)
            .writerId(writerId)
            .questionBoardContent(questionBoardContentEntity.toModel())
            .createdAt(createdAt)
            .build();
    }

    public static QuestionBoardEntity from(Post post) {
        return QuestionBoardEntity.builder()
            .id(post.getId())
            .questionId(post.getQuestionId())
            .writerId(post.getWriterId())
            .questionBoardContentEntity(QuestionBoardContentEntity.from(post.getQuestionBoardContent()))
            .createdAt(post.getCreatedAt())
            .build();
    }
}
