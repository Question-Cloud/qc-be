package com.eager.questioncloud.comment.entity;

import com.eager.questioncloud.comment.model.QuestionBoardComment;
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
@Table(name = "question_board_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBoardCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long boardId;

    @Column
    private Long writerId;

    @Column
    private String comment;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionBoardCommentEntity(Long id, Long boardId, Long writerId, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.boardId = boardId;
        this.writerId = writerId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public QuestionBoardComment toModel() {
        return QuestionBoardComment.builder()
            .id(id)
            .boardId(boardId)
            .writerId(writerId)
            .comment(comment)
            .createdAt(createdAt)
            .build();
    }
}
