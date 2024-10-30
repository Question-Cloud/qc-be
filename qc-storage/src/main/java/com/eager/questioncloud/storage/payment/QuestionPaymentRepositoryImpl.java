package com.eager.questioncloud.storage.payment;

import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.question.repository.QuestionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentRepositoryImpl implements QuestionPaymentRepository {
    private final QuestionPaymentJpaRepository questionPaymentJpaRepository;

    @Override
    public QuestionPayment save(QuestionPayment questionPayment) {
        return questionPaymentJpaRepository.save(QuestionPaymentEntity.from(questionPayment)).toModel();
    }
}
