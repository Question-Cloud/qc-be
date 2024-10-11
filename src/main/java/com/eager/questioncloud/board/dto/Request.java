package com.eager.questioncloud.board.dto;

import com.eager.questioncloud.board.domain.QuestionBoardFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterQuestionBoardRequest {
        @NotNull
        private Long questionId;

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private List<QuestionBoardFile> files = new ArrayList<>();
    }

    @Getter
    public static class ModifyQuestionBoardRequest {
        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private List<QuestionBoardFile> files = new ArrayList<>();
    }
}
