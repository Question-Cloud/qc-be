package com.eager.questioncloud.application.api.hub.question.dto;

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class HubQuestionControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class QuestionInformationResponse {
        private QuestionInformation question;
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionCategoriesResponse {
        private List<QuestionCategoryGroupBySubject> categories;
    }
}
