package com.eager.questioncloud.api.workspace;

import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class QuestionContentResponse {
        private QuestionContent question;
    }

    @Getter
    @AllArgsConstructor
    public static class CreatorProfileResponse {
        private CreatorProfile profile;
    }
}
