package com.eager.questioncloud.board;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionBoard {
    private Long id;
    private Long questionId;
    private Long writerId;
    private String title;
    private String comment;
    private List<QuestionBoardFile> files;
    private LocalDateTime createdAt;

    @Builder
    public QuestionBoard(Long id, Long questionId, Long writerId, String title, String comment, List<QuestionBoardFile> files,
        LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.title = title;
        this.comment = comment;
        this.files = files;
        this.createdAt = createdAt;
    }

    public QuestionBoardEntity toEntity() {
        return QuestionBoardEntity.builder()
            .id(id)
            .questionId(questionId)
            .writerId(writerId)
            .title(title)
            .comment(comment)
            .files(files)
            .createdAt(createdAt)
            .build();
    }
}
