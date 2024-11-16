package com.eager.questioncloud.core.domain.review.implement;

import com.eager.questioncloud.core.domain.question.event.RegisteredQuestionEvent;
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.domain.review.repository.QuestionReviewStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewStatisticsAppender {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    @EventListener
    public void append(RegisteredQuestionEvent event) {
        questionReviewStatisticsRepository.save(QuestionReviewStatistics.create(event.getQuestion().getId()));
    }
}
