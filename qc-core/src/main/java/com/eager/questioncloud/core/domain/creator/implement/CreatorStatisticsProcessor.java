package com.eager.questioncloud.core.domain.creator.implement;

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository;
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent;
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent;
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent;
import com.eager.questioncloud.core.domain.subscribe.event.SubscribedEvent;
import com.eager.questioncloud.core.domain.subscribe.event.UnsubscribedEvent;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorStatisticsProcessor {
    private final CreatorStatisticsRepository creatorStatisticsRepository;
    private final QuestionRepository questionRepository;
    private final LockManager lockManager;

    public void initializeCreatorStatistics(Long creatorId) {
        creatorStatisticsRepository.save(CreatorStatistics.create(creatorId));
    }

    @EventListener
    public void updateCreatorReviewStatistics(RegisteredReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        lockManager.executeWithLock(
            LockKeyGenerator.generateCreatorStatistics(question.getCreatorId()),
            () -> {
                CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
                creatorStatistics.updateReviewStatisticsByRegisteredReview(event.getRate());
                creatorStatisticsRepository.save(creatorStatistics);
            }
        );
    }

    @EventListener
    public void updateCreatorReviewStatistics(ModifiedReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        lockManager.executeWithLock(
            LockKeyGenerator.generateCreatorStatistics(question.getCreatorId()),
            () -> {
                CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
                creatorStatistics.updateReviewStatisticsByModifiedReview(event.getVarianceRate());
                creatorStatisticsRepository.save(creatorStatistics);
            }
        );
    }

    @EventListener
    public void updateCreatorReviewStatistics(DeletedReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        lockManager.executeWithLock(
            LockKeyGenerator.generateCreatorStatistics(question.getCreatorId()),
            () -> {
                CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
                creatorStatistics.updateReviewStatisticsByDeletedReview(event.getRate());
                creatorStatisticsRepository.save(creatorStatistics);
            }
        );
    }

    @EventListener
    public void increaseSubscribeCount(SubscribedEvent event) {
        creatorStatisticsRepository.increaseSubscribeCount(event.getCreatorId());
    }

    @EventListener
    public void decreaseSubscribeCount(UnsubscribedEvent event) {
        creatorStatisticsRepository.decreaseSubscribeCount(event.getCreatorId());
    }
}
