package com.eager.questioncloud.review.implement;

import com.eager.questioncloud.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.review.repository.QuestionReviewStatisticsRepository;
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
