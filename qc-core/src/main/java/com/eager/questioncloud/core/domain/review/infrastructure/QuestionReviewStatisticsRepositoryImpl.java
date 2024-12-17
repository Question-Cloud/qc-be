package com.eager.questioncloud.core.domain.review.infrastructure;

import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionReviewStatisticsRepositoryImpl implements QuestionReviewStatisticsRepository {
    private final QuestionReviewStatisticsJpaRepository questionReviewStatisticsJpaRepository;

    @Override
    public QuestionReviewStatistics get(Long questionId) {
        return questionReviewStatisticsJpaRepository.findById(questionId)
            .orElseThrow(() -> new CoreException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public QuestionReviewStatistics save(QuestionReviewStatistics questionReviewStatistics) {
        return questionReviewStatisticsJpaRepository.save(QuestionReviewStatisticsEntity.from(questionReviewStatistics)).toModel();
    }

    @Override
    public void deleteAllInBatch() {
        questionReviewStatisticsJpaRepository.deleteAllInBatch();
    }
}
