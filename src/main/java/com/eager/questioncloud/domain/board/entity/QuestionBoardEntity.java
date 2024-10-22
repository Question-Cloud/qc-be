package com.eager.questioncloud.domain.board.entity;

import com.eager.questioncloud.domain.board.model.QuestionBoard;
import com.eager.questioncloud.domain.board.vo.QuestionBoardContent;
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
    private QuestionBoardContent questionBoardContent;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionBoardEntity(Long id, Long questionId, Long writerId, QuestionBoardContent questionBoardContent, LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.questionBoardContent = questionBoardContent;
        this.createdAt = createdAt;
    }

    public QuestionBoard toModel() {
        return QuestionBoard.builder()
            .id(id)
            .questionId(questionId)
            .writerId(writerId)
            .questionBoardContent(questionBoardContent)
            .createdAt(createdAt)
            .build();
    }
}
