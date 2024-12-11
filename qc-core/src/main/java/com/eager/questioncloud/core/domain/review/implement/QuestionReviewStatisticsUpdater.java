package com.eager.questioncloud.core.domain.review.implement;

import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent;
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent;
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent;
import com.eager.questioncloud.core.domain.review.infrastructure.QuestionReviewStatisticsRepository;
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
    public void updateByModifiedReview(ModifiedReviewEvent event) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateReviewStatistics(event.getQuestionId()),
            () -> {
                QuestionReviewStatistics reviewStatistics = questionReviewStatisticsRepository.get(event.getQuestionId());
                reviewStatistics.updateByModifyReview(event.getVarianceRate());
                questionReviewStatisticsRepository.save(reviewStatistics);
            }
        );
    }

    @EventListener
    public void updateByDeletedReview(DeletedReviewEvent event) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateReviewStatistics(event.getQuestionId()),
            () -> {
                QuestionReviewStatistics reviewStatistics = questionReviewStatisticsRepository.get(event.getQuestionId());
                reviewStatistics.updateByDeleteReview(event.getRate());
                questionReviewStatisticsRepository.save(reviewStatistics);
            }
        );
    }
}
