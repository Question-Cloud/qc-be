package com.eager.questioncloud.comment.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class QuestionBoardCommentDto {
    @Getter
    @Builder
    public static class QuestionBoardCommentDetail {
        private Long id;
        private String writerName;
        private String profileImage;
        private String comment;
        private Boolean isCreator;
        private Boolean isWriter;
        private LocalDateTime createdAt;
    }
}
