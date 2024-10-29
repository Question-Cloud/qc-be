package com.eager.questioncloud.storage.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentJpaRepository extends JpaRepository<QuestionPaymentEntity, Long> {
}
