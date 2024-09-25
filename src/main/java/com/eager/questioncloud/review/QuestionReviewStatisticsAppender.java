package com.eager.questioncloud.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewStatisticsAppender {
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    public QuestionReviewStatistics append(QuestionReviewStatistics questionReviewStatistics) {
        return questionReviewStatisticsRepository.save(questionReviewStatistics);
    }
}
