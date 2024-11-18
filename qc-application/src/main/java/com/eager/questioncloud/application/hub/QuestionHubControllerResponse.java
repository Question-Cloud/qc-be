package com.eager.questioncloud.application.hub;

import com.eager.questioncloud.domain.question.QuestionCategoryGroupBySubject;
import com.eager.questioncloud.domain.question.QuestionInformation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionHubControllerResponse {
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
