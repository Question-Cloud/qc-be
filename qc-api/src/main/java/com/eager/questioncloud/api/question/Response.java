package com.eager.questioncloud.api.question;

import com.eager.questioncloud.core.domain.hub.question.vo.QuestionContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class QuestionContentResponse {
        private QuestionContent question;
    }
}
