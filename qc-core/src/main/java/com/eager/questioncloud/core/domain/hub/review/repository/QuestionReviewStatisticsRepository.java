package com.eager.questioncloud.core.domain.hub.review.repository;

import com.eager.questioncloud.core.domain.hub.review.model.QuestionReviewStatistics;

public interface QuestionReviewStatisticsRepository {
    QuestionReviewStatistics get(Long questionId);

    QuestionReviewStatistics save(QuestionReviewStatistics questionReviewStatistics);
}
