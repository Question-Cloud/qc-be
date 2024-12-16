package com.eager.questioncloud.core.domain.payment.infrastructure;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentRepositoryImpl implements QuestionPaymentRepository {
    private final QuestionPaymentJpaRepository questionPaymentJpaRepository;

    @Override
    public QuestionPayment save(QuestionPayment questionPayment) {
        QuestionPaymentEntity entity = questionPaymentJpaRepository.save(QuestionPaymentEntity.from(questionPayment));
        return QuestionPayment.builder()
            .id(entity.getId())
            .orderId(entity.getOrderId())
            .order(questionPayment.getOrder())
            .userId(entity.getUserId())
            .userCouponId(entity.getUserCouponId())
            .amount(entity.getAmount())
            .status(entity.getStatus())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    @Override
    public QuestionPayment findByPaymentId(String paymentId) {
        return questionPaymentJpaRepository.findByOrderId(paymentId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }
}
