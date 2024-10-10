package com.eager.questioncloud.payment.repository;

import com.eager.questioncloud.payment.entity.QuestionPaymentOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentOrderJpaRepository extends JpaRepository<QuestionPaymentOrderEntity, Long> {
}
