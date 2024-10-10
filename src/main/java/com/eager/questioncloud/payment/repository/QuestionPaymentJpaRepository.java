package com.eager.questioncloud.payment.repository;

import com.eager.questioncloud.payment.entity.QuestionPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentJpaRepository extends JpaRepository<QuestionPaymentEntity, Long> {
}
