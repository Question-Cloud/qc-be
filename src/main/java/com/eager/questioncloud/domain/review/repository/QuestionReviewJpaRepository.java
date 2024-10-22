package com.eager.questioncloud.domain.review.repository;

import com.eager.questioncloud.domain.review.entity.QuestionReviewEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReviewJpaRepository extends JpaRepository<QuestionReviewEntity, Long> {
    Optional<QuestionReviewEntity> findByQuestionIdAndReviewerIdAndIsDeletedFalse(Long questionId, Long reviewerId);

    Optional<QuestionReviewEntity> findByIdAndReviewerIdAndIsDeletedFalse(Long id, Long reviewerId);
}
