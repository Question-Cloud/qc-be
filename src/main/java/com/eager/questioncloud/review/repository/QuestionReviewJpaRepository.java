package com.eager.questioncloud.review.repository;

import com.eager.questioncloud.review.entity.QuestionReviewEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReviewJpaRepository extends JpaRepository<QuestionReviewEntity, Long> {
    Optional<QuestionReviewEntity> findByQuestionIdAndReviewerIdAndIsDeletedFalse(Long questionId, Long reviewerId);

    Optional<QuestionReviewEntity> findByIdAndReviewerIdAndIsDeletedFalse(Long id, Long reviewerId);
}
