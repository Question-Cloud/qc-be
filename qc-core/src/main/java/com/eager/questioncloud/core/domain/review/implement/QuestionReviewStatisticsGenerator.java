package com.eager.questioncloud.core.domain.review.implement;

import com.eager.questioncloud.core.domain.question.event.RegisteredQuestionEvent;
import com.eager.questioncloud.core.domain.review.infrastructure.QuestionReviewStatisticsRepository;
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewStatisticsGenerator {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    @EventListener
    public void generate(RegisteredQuestionEvent event) {
        questionReviewStatisticsRepository.save(QuestionReviewStatistics.create(event.getQuestion().getId()));
    }
}
