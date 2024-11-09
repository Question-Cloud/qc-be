package com.eager.questioncloud.core.domain.hub.review.event;

import com.eager.questioncloud.core.domain.hub.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.domain.hub.review.repository.QuestionReviewStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateReviewStatisticsEventHandler {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    @EventListener
    public void update(UpdateReviewStatisticsEvent updateReviewStatisticsEvent) {
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(updateReviewStatisticsEvent.getQuestionId());
        questionReviewStatistics.update(updateReviewStatisticsEvent);
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }
}
