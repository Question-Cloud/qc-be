package com.eager.questioncloud.core.domain.review.implement;

import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.domain.review.repository.QuestionReviewStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewStatisticsUpdater {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    //TODO 분산락 로직 추가 시 복구
    //@DistributedLock(key = "'REVIEW-STATISTICS:' + #questionId")
    public void updateByNewReview(Long questionId, int newRate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByNewReview(newRate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }

    //TODO 분산락 로직 추가 시 복구
    //@DistributedLock(key = "'REVIEW-STATISTICS:' + #questionId")
    public void updateByModifyReview(Long questionId, int fluctuationRate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByModifyReview(fluctuationRate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }

    //TODO 분산락 로직 추가 시 복구
    //@DistributedLock(key = "'REVIEW-STATISTICS:' + #questionId")
    public void updateByDeleteReview(Long questionId, int rate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByDeleteReview(rate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }
}
