package com.eager.questioncloud.question.dto;

import com.eager.questioncloud.question.dto.QuestionCategoryDto.QuestionCategoryListItem;
import com.eager.questioncloud.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.question.vo.QuestionContent;
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
