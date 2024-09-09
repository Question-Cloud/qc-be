package com.eager.questioncloud.board;

import java.util.List;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterQuestionBoardRequest {
        private String title;
        private String comment;
        private List<QuestionBoardFile> files;
    }

    @Getter
    public static class ModifyQuestionBoardRequest {
        private String title;
        private String content;
        private List<QuestionBoardFile> files;
    }
}
