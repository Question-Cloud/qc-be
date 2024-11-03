package com.eager.questioncloud.core.domain.hub.board.model;

import com.eager.questioncloud.core.domain.hub.board.vo.QuestionBoardContent;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {
    private Long id;
    private Long questionId;
    private Long writerId;
    private QuestionBoardContent questionBoardContent;
    private LocalDateTime createdAt;

    @Builder
    public Post(Long id, Long questionId, Long writerId, QuestionBoardContent questionBoardContent, LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.questionBoardContent = questionBoardContent;
        this.createdAt = createdAt;
    }

    public static Post create(Long questionId, Long writerId, QuestionBoardContent questionBoardContent) {
        return Post.builder()
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
