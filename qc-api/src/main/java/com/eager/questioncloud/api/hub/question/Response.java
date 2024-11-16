package com.eager.questioncloud.api.hub.question;

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
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
