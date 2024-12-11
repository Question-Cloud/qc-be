package com.eager.questioncloud.application.api.hub;

import com.eager.questioncloud.core.domain.review.QuestionReview;
import com.eager.questioncloud.core.domain.review.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionHunReviewUpdater {
    private final QuestionReviewRepository questionReviewRepository;

    public int modifyQuestionReview(QuestionReview questionReview, String comment, int rate) {
        int varianceRate = rate - questionReview.getRate();
        questionReview.modify(comment, rate);
        questionReviewRepository.save(questionReview);
        return varianceRate;
    }
}
