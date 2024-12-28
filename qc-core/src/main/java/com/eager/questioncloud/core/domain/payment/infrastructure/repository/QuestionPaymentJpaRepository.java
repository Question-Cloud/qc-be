package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionPaymentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPaymentJpaRepository extends JpaRepository<QuestionPaymentEntity, Long> {
    Optional<QuestionPaymentEntity> findByOrderId(String orderId);

    int countByUserId(Long userId);
}
