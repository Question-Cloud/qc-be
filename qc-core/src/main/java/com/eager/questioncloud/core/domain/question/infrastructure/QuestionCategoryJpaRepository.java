package com.eager.questioncloud.core.domain.question.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCategoryJpaRepository extends JpaRepository<QuestionCategoryEntity, Long> {
}
