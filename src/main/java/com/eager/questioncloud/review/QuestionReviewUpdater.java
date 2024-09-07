package com.eager.questioncloud.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewUpdater {
    private final QuestionReviewRepository questionReviewRepository;

    public void update(Long reviewId, Long userId, String comment, int rate) {
        QuestionReview questionReview = questionReviewRepository.getForModify(reviewId, userId);
        questionReview.modify(comment, rate);
        questionReviewRepository.save(questionReview);
    }
}
