package com.eager.questioncloud.core.domain.review;

import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewStatisticsUpdater {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;
    private final LockManager lockManager;

    @EventListener
    public void updateByRegisteredReview(RegisteredReviewEvent event) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateReviewStatistics(event.getQuestionId()),
            () -> {
                QuestionReviewStatistics reviewStatistics = questionReviewStatisticsRepository.get(event.getQuestionId());
                reviewStatistics.updateByNewReview(event.getRate());
                questionReviewStatisticsRepository.save(reviewStatistics);
            }
        );
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
