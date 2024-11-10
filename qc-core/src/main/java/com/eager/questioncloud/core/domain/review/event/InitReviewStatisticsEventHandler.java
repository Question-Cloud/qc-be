package com.eager.questioncloud.core.domain.review.event;

import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.domain.review.repository.QuestionReviewStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitReviewStatisticsEventHandler {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    @EventListener
    public void init(InitReviewStatisticsEvent initReviewStatisticsEvent) {
        QuestionReviewStatistics questionReviewStatistics = QuestionReviewStatistics.create(initReviewStatisticsEvent.getQuestionId());
        questionReviewStatisticsRepository.save(questionReviewStatistics);
    }
}
