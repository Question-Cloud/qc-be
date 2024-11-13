package com.eager.questioncloud.storage.payment;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.repository.QuestionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentRepositoryImpl implements QuestionPaymentRepository {
    private final QuestionPaymentJpaRepository questionPaymentJpaRepository;

    @Override
    public QuestionPayment save(QuestionPayment questionPayment) {
        QuestionPaymentEntity result = questionPaymentJpaRepository.save(QuestionPaymentEntity.from(questionPayment));
        return QuestionPayment.builder()
            .id(result.getId())
            .userId(result.getUserId())
            .paymentId(result.getPaymentId())
            .orders(questionPayment.getOrders())
            .userCoupon(questionPayment.getUserCoupon())
            .amount(result.getAmount())
            .status(result.getStatus())
            .createdAt(result.getCreatedAt())
            .build();
    }
}
