package com.eager.questioncloud.core.domain.review;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionReviewStatisticsRepositoryImpl implements QuestionReviewStatisticsRepository {
    private final QuestionReviewStatisticsJpaRepository questionReviewStatisticsJpaRepository;

    @Override
    public QuestionReviewStatistics get(Long questionId) {
        return questionReviewStatisticsJpaRepository.findById(questionId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public QuestionReviewStatistics save(QuestionReviewStatistics questionReviewStatistics) {
        return questionReviewStatisticsJpaRepository.save(QuestionReviewStatisticsEntity.from(questionReviewStatistics)).toModel();
    }
}
