package com.eager.questioncloud.storage.question;

import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.domain.review.repository.QuestionReviewStatisticsRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Override
    public Map<Long, QuestionReviewStatistics> findByQuestionIdIn(List<Long> questionIds) {
        List<QuestionReviewStatistics> result = questionReviewStatisticsJpaRepository.findByQuestionIdIn(questionIds)
            .stream()
            .map(QuestionReviewStatisticsEntity::toModel)
            .toList();

        return result.stream()
            .collect(Collectors.toMap(QuestionReviewStatistics::getQuestionId, data -> data));
    }
}
