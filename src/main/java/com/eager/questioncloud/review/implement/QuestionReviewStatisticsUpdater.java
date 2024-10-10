package com.eager.questioncloud.review.implement;

import com.eager.questioncloud.annotation.DistributedLock;
import com.eager.questioncloud.review.domain.QuestionReviewStatistics;
import com.eager.questioncloud.review.repository.QuestionReviewStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewStatisticsUpdater {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    @DistributedLock(key = "'REVIEW-STATISTICS:' + #questionId")
    public void updateByNewReview(Long questionId, int newRate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByNewReview(newRate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }

    @DistributedLock(key = "'REVIEW-STATISTICS:' + #questionId")
    public void updateByModifyReview(Long questionId, int fluctuationRate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByModifyReview(fluctuationRate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }

    @DistributedLock(key = "'REVIEW-STATISTICS:' + #questionId")
    public void updateByDeleteReview(Long questionId, int rate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByDeleteReview(rate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }
}
