package com.eager.questioncloud.comment;

import lombok.Getter;

public class Request {
    @Getter
    public static class AddQuestionBoardCommentRequest {
        private Long boardId;
        private String comment;
    }
}
