package com.eager.questioncloud.domain.payment;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentJpaRepository extends JpaRepository<QuestionPaymentEntity, Long> {
    Optional<QuestionPaymentEntity> findByPaymentId(String paymentId);
}
