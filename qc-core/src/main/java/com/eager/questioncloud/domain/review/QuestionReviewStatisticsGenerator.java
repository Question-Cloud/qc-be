package com.eager.questioncloud.domain.review;

import com.eager.questioncloud.domain.question.RegisteredQuestionEvent;
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
