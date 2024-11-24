package com.eager.questioncloud.core.domain.review;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewStatisticsUpdater {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    @EventListener
    @Transactional
    public void updateByRegisteredReview(RegisteredReviewEvent event) {
        QuestionReviewStatistics reviewStatistics = questionReviewStatisticsRepository.get(event.getQuestionId());
        reviewStatistics.updateByNewReview(event.getRate());
        questionReviewStatisticsRepository.save(reviewStatistics);
    }

    @EventListener
    @Transactional
    public void updateByModifiedReview(ModifiedReviewEvent event) {
        QuestionReviewStatistics reviewStatistics = questionReviewStatisticsRepository.get(event.getQuestionId());
        reviewStatistics.updateByModifyReview(event.getVarianceRate());
        questionReviewStatisticsRepository.save(reviewStatistics);
    }

    @EventListener
    @Transactional
    public void updateByDeletedReview(DeletedReviewEvent event) {
        QuestionReviewStatistics reviewStatistics = questionReviewStatisticsRepository.get(event.getQuestionId());
        reviewStatistics.updateByDeleteReview(event.getRate());
        questionReviewStatisticsRepository.save(reviewStatistics);
    }
}
