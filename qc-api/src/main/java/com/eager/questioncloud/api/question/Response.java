package com.eager.questioncloud.api.question;

import com.eager.questioncloud.core.domain.questionhub.board.dto.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.core.domain.questionhub.question.dto.QuestionCategoryDto.QuestionCategoryListItem;
import com.eager.questioncloud.core.domain.questionhub.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.questionhub.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.questionhub.review.dto.QuestionReviewDto.MyQuestionReview;
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

    @Getter
    @AllArgsConstructor
    public static class MyQuestionReviewResponse {
        private MyQuestionReview review;
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionBoardResponse {
        private QuestionBoardDetail board;
    }
}
