package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionOrderEntity;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionOrderRepositoryImpl implements QuestionOrderRepository {
    private final QuestionOrderJpaRepository questionOrderJpaRepository;

    @Override
    public void save(QuestionOrder questionOrder) {
        questionOrderJpaRepository.saveAll(QuestionOrderEntity.from(questionOrder));
    }

    @Override
    public void deleteAllInBatch() {
        questionOrderJpaRepository.deleteAllInBatch();
    }
}
