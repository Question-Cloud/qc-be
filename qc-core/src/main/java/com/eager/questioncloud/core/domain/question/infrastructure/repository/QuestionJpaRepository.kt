package com.eager.questioncloud.core.domain.question.infrastructure.repository;

import com.eager.questioncloud.core.domain.question.infrastructure.entity.QuestionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {
    Optional<QuestionEntity> findByIdAndCreatorId(Long id, Long creatorId);
}
