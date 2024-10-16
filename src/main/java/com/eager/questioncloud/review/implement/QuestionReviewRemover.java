package com.eager.questioncloud.review.implement;

import com.eager.questioncloud.annotation.DistributedLock;
import com.eager.questioncloud.review.model.QuestionReview;
import com.eager.questioncloud.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewRemover {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewStatisticsUpdater questionReviewStatisticsUpdater;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @DistributedLock(key = "'REVIEW:' + #reviewId")
    public void delete(Long reviewId, Long userId) {
        QuestionReview review = questionReviewRepository.getForModifyAndDelete(reviewId, userId);
        review.delete();
        questionReviewRepository.save(review);
        questionReviewStatisticsUpdater.updateByDeleteReview(review.getQuestionId(), review.getRate());
    }
}
