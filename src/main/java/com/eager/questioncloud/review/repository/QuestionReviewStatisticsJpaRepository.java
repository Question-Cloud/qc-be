package com.eager.questioncloud.review.repository;

import com.eager.questioncloud.review.entity.QuestionReviewStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReviewStatisticsJpaRepository extends JpaRepository<QuestionReviewStatisticsEntity, Long> {
}
