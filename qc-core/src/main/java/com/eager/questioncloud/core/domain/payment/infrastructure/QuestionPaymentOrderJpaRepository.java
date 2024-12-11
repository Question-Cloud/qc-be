package com.eager.questioncloud.core.domain.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentOrderJpaRepository extends JpaRepository<QuestionPaymentOrderEntity, Long> {
}
