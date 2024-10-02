package com.eager.questioncloud.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewRemover {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewStatisticsUpdater questionReviewStatisticsUpdater;

    public void delete(Long reviewId, Long userId) {
        QuestionReview review = questionReviewRepository.getForModifyAndDelete(reviewId, userId);
        review.delete();
        questionReviewRepository.save(review);
        questionReviewStatisticsUpdater.updateByDeleteReview(review.getQuestionId(), review.getRate());
    }
}
