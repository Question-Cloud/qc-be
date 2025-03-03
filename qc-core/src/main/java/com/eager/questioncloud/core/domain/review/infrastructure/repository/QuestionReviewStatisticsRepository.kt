package com.eager.questioncloud.core.domain.review.infrastructure.repository;

import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;

public interface QuestionReviewStatisticsRepository {
    QuestionReviewStatistics get(Long questionId);

    QuestionReviewStatistics save(QuestionReviewStatistics questionReviewStatistics);

    void deleteAllInBatch();
}
