package com.eager.questioncloud.review;

import com.eager.questioncloud.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewRemover {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewStatisticsUpdater questionReviewStatisticsUpdater;

    @Transactional
    @DistributedLock(key = "'REVIEW:' + #reviewId")
    public void delete(Long reviewId, Long userId) {
        QuestionReview review = questionReviewRepository.getForModifyAndDelete(reviewId, userId);
        review.delete();
        questionReviewRepository.save(review);
        questionReviewStatisticsUpdater.updateByDeleteReview(review.getQuestionId(), review.getRate());
    }
}
