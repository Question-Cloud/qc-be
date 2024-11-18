package com.eager.questioncloud.domain.review.dto;

import com.eager.questioncloud.domain.review.model.QuestionReview;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyQuestionReview {
    private Long id;
    private Integer rate;
    private String comment;

    public static MyQuestionReview from(QuestionReview questionReview) {
        return new MyQuestionReview(questionReview.getId(), questionReview.getRate(), questionReview.getComment());
    }
}
