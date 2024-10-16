package com.eager.questioncloud.review.repository;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.review.model.QuestionReviewStatistics;
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
        return questionReviewStatisticsJpaRepository.save(questionReviewStatistics.toEntity()).toModel();
    }
}
