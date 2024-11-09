package com.eager.questioncloud.core.domain.hub.review.implement;

import com.eager.questioncloud.core.domain.hub.review.dto.QuestionReviewUpdateResult;
import com.eager.questioncloud.core.domain.hub.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.hub.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewUpdater {
    private final QuestionReviewRepository questionReviewRepository;

    public QuestionReviewUpdateResult update(Long reviewId, Long userId, String comment, int rate) {
        QuestionReview questionReview = questionReviewRepository.getForModifyAndDelete(reviewId, userId);
        int varianceRate = rate - questionReview.getRate();
        questionReview.modify(comment, rate);
        questionReviewRepository.save(questionReview);
        return new QuestionReviewUpdateResult(questionReview.getQuestionId(), varianceRate);
    }
}
