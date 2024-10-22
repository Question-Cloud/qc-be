package com.eager.questioncloud.domain.payment.repository;

import com.eager.questioncloud.domain.payment.entity.QuestionPaymentOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentOrderJpaRepository extends JpaRepository<QuestionPaymentOrderEntity, Long> {
}
