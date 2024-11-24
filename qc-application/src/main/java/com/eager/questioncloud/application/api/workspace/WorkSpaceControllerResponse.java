package com.eager.questioncloud.application.api.workspace;

import com.eager.questioncloud.core.domain.creator.CreatorProfile;
import com.eager.questioncloud.core.domain.question.QuestionContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class WorkSpaceControllerResponse {
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
