package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionCategoryDto.QuestionCategoryListItem;
import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class QuestionDetailResponse {
        private QuestionDetail question;
        private Boolean isOwned;
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionCategoriesResponse {
        private List<QuestionCategoryListItem> categories;
    }
}
