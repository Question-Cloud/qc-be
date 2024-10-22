package com.eager.questioncloud.domain.payment.repository;

import com.eager.questioncloud.domain.payment.model.QuestionPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentRepositoryImpl implements QuestionPaymentRepository {
    private final QuestionPaymentJpaRepository questionPaymentJpaRepository;

    @Override
    public QuestionPayment save(QuestionPayment questionPayment) {
        return questionPaymentJpaRepository.save(questionPayment.toEntity()).toModel();
    }
}
