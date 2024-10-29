package com.eager.questioncloud.core.domain.board.model;

import com.eager.questioncloud.core.domain.board.vo.QuestionBoardContent;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionBoard {
    private Long id;
    private Long questionId;
    private Long writerId;
    private QuestionBoardContent questionBoardContent;
    private LocalDateTime createdAt;

    @Builder
    public QuestionBoard(Long id, Long questionId, Long writerId, QuestionBoardContent questionBoardContent, LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.questionBoardContent = questionBoardContent;
        this.createdAt = createdAt;
    }

    public static QuestionBoard create(Long questionId, Long writerId, QuestionBoardContent questionBoardContent) {
        return QuestionBoard.builder()
            .questionId(questionId)
            .writerId(writerId)
            .questionBoardContent(questionBoardContent)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void updateQuestionBoardContent(QuestionBoardContent questionBoardContent) {
        this.questionBoardContent = questionBoardContent;
    }
}
