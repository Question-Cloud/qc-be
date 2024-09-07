package com.eager.questioncloud.review;

import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterQuestionReviewRequest {
        private Long questionId;
        private int rate;
        private String comment;
    }

    @Getter
    public static class ModifyQuestionReviewRequest {
        private int rate;
        private String comment;
    }
}
