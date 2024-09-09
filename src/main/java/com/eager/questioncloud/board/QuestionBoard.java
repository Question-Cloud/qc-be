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
    private String content;
    private List<QuestionBoardFile> files;
    private LocalDateTime createdAt;

    @Builder
    public QuestionBoard(Long id, Long questionId, Long writerId, String title, String content, List<QuestionBoardFile> files,
        LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.files = files;
        this.createdAt = createdAt;
    }

    public static QuestionBoard create(Long questionId, Long writerId, String title, String content, List<QuestionBoardFile> files) {
        return QuestionBoard.builder()
            .questionId(questionId)
            .writerId(writerId)
            .title(title)
            .content(content)
            .files(files)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public QuestionBoardEntity toEntity() {
        return QuestionBoardEntity.builder()
            .id(id)
            .questionId(questionId)
            .writerId(writerId)
            .title(title)
            .content(content)
            .files(files)
            .createdAt(createdAt)
            .build();
    }
}
