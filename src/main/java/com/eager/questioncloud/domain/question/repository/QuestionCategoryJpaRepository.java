package com.eager.questioncloud.domain.question.repository;

import com.eager.questioncloud.domain.question.entity.QuestionCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCategoryJpaRepository extends JpaRepository<QuestionCategoryEntity, Long> {
}
