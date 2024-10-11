package com.eager.questioncloud.review.repository;

import com.eager.questioncloud.review.domain.QuestionReviewStatistics;

public interface QuestionReviewStatisticsRepository {
    QuestionReviewStatistics get(Long questionId);

    QuestionReviewStatistics save(QuestionReviewStatistics questionReviewStatistics);
}