package com.eager.questioncloud.core.domain.review.infrastructure.repository;

import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewStatisticsEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReviewStatisticsJpaRepository extends JpaRepository<QuestionReviewStatisticsEntity, Long> {
    List<QuestionReviewStatisticsEntity> findByQuestionIdIn(List<Long> questionIds);
}
