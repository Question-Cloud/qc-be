package com.eager.questioncloud.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class Request {
    @Getter
    public static class AddQuestionBoardCommentRequest {
        @NotNull
        private Long boardId;

        @NotBlank
        private String comment;
    }

    @Getter
    public static class ModifyQuestionBoardCommentRequest {
        @NotBlank
        private String comment;
    }
}
