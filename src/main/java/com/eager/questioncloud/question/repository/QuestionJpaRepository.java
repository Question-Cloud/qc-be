package com.eager.questioncloud.question.repository;

import com.eager.questioncloud.question.entity.QuestionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {
    Optional<QuestionEntity> findByIdAndCreatorId(Long id, Long creatorId);
}
