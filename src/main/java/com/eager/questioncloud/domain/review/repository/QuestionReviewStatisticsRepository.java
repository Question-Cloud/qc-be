package com.eager.questioncloud.domain.review.repository;

import com.eager.questioncloud.domain.review.model.QuestionReviewStatistics;

public interface QuestionReviewStatisticsRepository {
    QuestionReviewStatistics get(Long questionId);

    QuestionReviewStatistics save(QuestionReviewStatistics questionReviewStatistics);
}
