package com.eager.questioncloud.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentOrderJpaRepository extends JpaRepository<QuestionPaymentOrderEntity, Long> {
}
