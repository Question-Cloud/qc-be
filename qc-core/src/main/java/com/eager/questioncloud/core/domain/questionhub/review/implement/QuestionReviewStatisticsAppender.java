package com.eager.questioncloud.core.domain.questionhub.review.implement;

import com.eager.questioncloud.core.domain.questionhub.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.domain.questionhub.review.repository.QuestionReviewStatisticsRepository;
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
