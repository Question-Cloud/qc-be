package com.eager.questioncloud.core.domain.payment.infrastructure;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentRepositoryImpl implements QuestionPaymentRepository {
    private final QuestionPaymentJpaRepository questionPaymentJpaRepository;

    @Override
    public QuestionPayment save(QuestionPayment questionPayment) {
        QuestionPaymentEntity entity = questionPaymentJpaRepository.save(QuestionPaymentEntity.from(questionPayment));
        questionPayment.success(entity.getId());
        return questionPayment;
    }
}
