package com.eager.questioncloud.application.business.review.implement;

import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HunReviewUpdater {
    private final QuestionReviewRepository questionReviewRepository;

    public int modifyQuestionReview(QuestionReview questionReview, String comment, int rate) {
        int varianceRate = rate - questionReview.getRate();
        questionReview.modify(comment, rate);
        questionReviewRepository.save(questionReview);
        return varianceRate;
    }
}
