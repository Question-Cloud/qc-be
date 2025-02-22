package com.eager.questioncloud.application.api.workspace.dto;

import com.eager.questioncloud.core.domain.creator.model.CreatorProfile;
import com.eager.questioncloud.core.domain.question.model.QuestionContent;
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

    @AllArgsConstructor
    @Getter
    public static class RegisterCreatorResponse {
        private Long creatorId;
    }
}
