package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionCategoryDto.QuestionCategoryListItem;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import com.eager.questioncloud.question.QuestionReviewDto.MyQuestionReview;
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
    public static class MyQuestionReviewResponse {
        private MyQuestionReview review;
    }
}
