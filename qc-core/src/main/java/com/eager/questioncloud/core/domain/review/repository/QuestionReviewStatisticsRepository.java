package com.eager.questioncloud.core.domain.review.repository;

import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import java.util.List;
import java.util.Map;

public interface QuestionReviewStatisticsRepository {
    QuestionReviewStatistics get(Long questionId);

    QuestionReviewStatistics save(QuestionReviewStatistics questionReviewStatistics);

    Map<Long, QuestionReviewStatistics> findByQuestionIdIn(List<Long> questionIds);
}
