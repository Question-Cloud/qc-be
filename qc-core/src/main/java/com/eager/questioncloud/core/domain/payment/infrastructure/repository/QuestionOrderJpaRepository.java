package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOrderJpaRepository extends JpaRepository<QuestionOrderEntity, Long> {
}
