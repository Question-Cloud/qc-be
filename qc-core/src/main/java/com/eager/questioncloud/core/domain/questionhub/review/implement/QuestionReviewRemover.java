package com.eager.questioncloud.core.domain.questionhub.review.implement;

import com.eager.questioncloud.core.domain.questionhub.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.questionhub.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewRemover {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewStatisticsUpdater questionReviewStatisticsUpdater;

    //TODO 분산락 로직 추가 시 복구
    @Transactional(isolation = Isolation.READ_COMMITTED)
//    @DistributedLock(key = "'REVIEW:' + #reviewId")
    public void delete(Long reviewId, Long userId) {
        QuestionReview review = questionReviewRepository.getForModifyAndDelete(reviewId, userId);
        review.delete();
        questionReviewRepository.save(review);
        questionReviewStatisticsUpdater.updateByDeleteReview(review.getQuestionId(), review.getRate());
    }
}
