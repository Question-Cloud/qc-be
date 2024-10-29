package com.eager.questioncloud.core.domain.questionhub.review.repository;

import com.eager.questioncloud.core.domain.questionhub.review.model.QuestionReviewStatistics;

public interface QuestionReviewStatisticsRepository {
    QuestionReviewStatistics get(Long questionId);

    QuestionReviewStatistics save(QuestionReviewStatistics questionReviewStatistics);
}
