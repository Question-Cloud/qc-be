package com.eager.questioncloud.domain.question.dto;

import com.eager.questioncloud.domain.question.vo.QuestionContent;
import com.eager.questioncloud.domain.question.dto.QuestionCategoryDto.QuestionCategoryListItem;
import com.eager.questioncloud.domain.question.dto.QuestionDto.QuestionInformation;
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
        private List<QuestionCategoryListItem> categories;
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionContentResponse {
        private QuestionContent question;
    }
}
