package com.eager.questioncloud.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentRepositoryImpl implements QuestionPaymentRepository {
    private final QuestionPaymentJpaRepository questionPaymentJpaRepository;

    @Override
    public QuestionPayment append(QuestionPayment questionPayment) {
        return questionPaymentJpaRepository.save(questionPayment.toEntity()).toModel();
    }
}
