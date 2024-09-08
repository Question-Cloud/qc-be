package com.eager.questioncloud.board;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
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

    @Column
    private String title;

    @Column
    private String comment;

    @Convert(converter = QuestionBoardFileConverter.class)
    private List<QuestionBoardFile> files;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionBoardEntity(Long id, Long questionId, Long writerId, String title, String comment, List<QuestionBoardFile> files,
        LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.title = title;
        this.comment = comment;
        this.files = files;
        this.createdAt = createdAt;
    }

    public QuestionBoard toModel() {
        return QuestionBoard.builder()
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
