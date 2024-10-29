package com.eager.questioncloud.storage.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentOrderJpaRepository extends JpaRepository<QuestionPaymentOrderEntity, Long> {
}
