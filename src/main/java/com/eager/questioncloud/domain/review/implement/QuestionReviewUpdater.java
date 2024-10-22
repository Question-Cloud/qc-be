package com.eager.questioncloud.domain.review.implement;

import com.eager.questioncloud.annotation.DistributedLock;
import com.eager.questioncloud.domain.review.model.QuestionReview;
import com.eager.questioncloud.domain.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewUpdater {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewStatisticsUpdater questionReviewStatisticsUpdater;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @DistributedLock(key = "'REVIEW:' + #reviewId")
    public void update(Long reviewId, Long userId, String comment, int rate) {
        QuestionReview questionReview = questionReviewRepository.getForModifyAndDelete(reviewId, userId);
        int fluctuationRate = rate - questionReview.getRate();
        questionReview.modify(comment, rate);
        questionReviewRepository.save(questionReview);
        questionReviewStatisticsUpdater.updateByModifyReview(questionReview.getQuestionId(), fluctuationRate);
    }
}
