package com.eager.questioncloud.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewStatisticsUpdater {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    public void updateByNewReview(Long questionId, int newRate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByNewReview(newRate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }

    public void updateByModifyReview(Long questionId, int fluctuationRate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByModifyReview(fluctuationRate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }

    public void updateByDeleteReview(Long questionId, int rate) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(questionId);
        questionReviewStatistics.updateByDeleteReview(rate);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }
}
