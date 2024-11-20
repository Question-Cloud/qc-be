package com.eager.questioncloud.domain.creator;

import com.eager.questioncloud.domain.question.Question;
import com.eager.questioncloud.domain.question.QuestionRepository;
import com.eager.questioncloud.domain.review.DeletedReviewEvent;
import com.eager.questioncloud.domain.review.ModifiedReviewEvent;
import com.eager.questioncloud.domain.review.RegisteredReviewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorStatisticsUpdater {
    private final CreatorStatisticsRepository creatorStatisticsRepository;
    private final QuestionRepository questionRepository;

    @EventListener
    public void updateCreatorReviewStatistics(RegisteredReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
        creatorStatistics.updateReviewStatisticsByRegisteredReview(event.getRate());
        creatorStatisticsRepository.save(creatorStatistics);
    }

    @EventListener
    public void updateCreatorReviewStatistics(ModifiedReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
        creatorStatistics.updateReviewStatisticsByModifiedReview(event.getVarianceRate());
        creatorStatisticsRepository.save(creatorStatistics);
    }

    @EventListener
    public void updateCreatorReviewStatistics(DeletedReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
        creatorStatistics.updateReviewStatisticsByDeletedReview(event.getRate());
        creatorStatisticsRepository.save(creatorStatistics);
    }
}
