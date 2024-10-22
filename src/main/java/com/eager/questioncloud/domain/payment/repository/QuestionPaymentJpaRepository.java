package com.eager.questioncloud.domain.payment.repository;

import com.eager.questioncloud.domain.payment.entity.QuestionPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentJpaRepository extends JpaRepository<QuestionPaymentEntity, Long> {
}
