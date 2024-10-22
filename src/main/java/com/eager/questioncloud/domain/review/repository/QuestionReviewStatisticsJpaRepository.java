package com.eager.questioncloud.domain.review.repository;

import com.eager.questioncloud.domain.review.entity.QuestionReviewStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReviewStatisticsJpaRepository extends JpaRepository<QuestionReviewStatisticsEntity, Long> {
}
